package com.marcapro.backend.service.impl;

import com.marcapro.backend.dto.NearbyPointDto;
import com.marcapro.backend.dto.PropertyDto;
import com.marcapro.backend.entity.NearbyPoint;
import com.marcapro.backend.entity.Property;
import com.marcapro.backend.enums.PropertyStatus;
import com.marcapro.backend.enums.PropertyType;
import com.marcapro.backend.exception.ResourceNotFoundException;
import com.marcapro.backend.repository.NearbyPointRepository;
import com.marcapro.backend.repository.PropertyRepository;
import com.marcapro.backend.service.PropertyService;
import com.marcapro.backend.storage.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final NearbyPointRepository nearbyPointRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public PropertyDto.Response create(PropertyDto.Request request) {
        Property property = mapRequestToEntity(new Property(), request);
        Property saved = propertyRepository.save(property);
        saveNearbyPoints(saved, request.nearbyPoints());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyDto.Response findById(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada: " + id));
        return mapToResponse(property);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDto.Summary> search(PropertyType type, PropertyStatus status,
                                             BigDecimal minPrice, BigDecimal maxPrice,
                                             String city, Integer bedrooms, Pageable pageable) {
        return propertyRepository.search(
                type != null ? type.name() : null,
                status != null ? status.name() : null,
                minPrice, maxPrice, city, bedrooms, pageable)
            .map(this::mapToSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto.Summary> findFeatured() {
        return propertyRepository.findByFeaturedTrueAndStatus(PropertyStatus.DISPONIBLE)
            .stream().map(this::mapToSummary).collect(Collectors.toList());
    }

    @Override
    public PropertyDto.Response update(Long id, PropertyDto.Request request) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada: " + id));
        mapRequestToEntity(property, request);
        nearbyPointRepository.deleteByPropertyId(id);
        saveNearbyPoints(property, request.nearbyPoints());
        return mapToResponse(propertyRepository.save(property));
    }

    @Override
    public void delete(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada: " + id));
        property.getPhotoUrls().forEach(cloudinaryService::deleteByUrl);
        propertyRepository.delete(property);
    }

    @Override
    public PropertyDto.Response addPhotos(Long id, List<MultipartFile> photos) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada: " + id));
        String folder = "marcapro/properties/" + id;
        List<String> uploadedUrls = photos.stream()
            .map(f -> cloudinaryService.uploadImage(f, folder))
            .collect(Collectors.toList());
        property.getPhotoUrls().addAll(uploadedUrls);
        if (property.getCoverPhotoUrl() == null && !uploadedUrls.isEmpty()) {
            property.setCoverPhotoUrl(uploadedUrls.get(0));
        }
        return mapToResponse(propertyRepository.save(property));
    }

    @Override
    public PropertyDto.Response setCoverPhoto(Long id, String photoUrl) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada: " + id));
        property.setCoverPhotoUrl(photoUrl);
        return mapToResponse(propertyRepository.save(property));
    }

    @Override
    public PropertyDto.Response removePhoto(Long id, String photoUrl) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada: " + id));
        property.getPhotoUrls().remove(photoUrl);
        if (photoUrl.equals(property.getCoverPhotoUrl())) {
            property.setCoverPhotoUrl(property.getPhotoUrls().isEmpty() ? null : property.getPhotoUrls().get(0));
        }
        cloudinaryService.deleteByUrl(photoUrl);
        return mapToResponse(propertyRepository.save(property));
    }

    private Property mapRequestToEntity(Property property, PropertyDto.Request request) {
        property.setTitle(request.title());
        property.setDescription(request.description());
        property.setPrice(request.price());
        property.setType(request.type());
        if (request.status() != null) property.setStatus(request.status());
        property.setAreaSqm(request.areaSqm());
        property.setBedrooms(request.bedrooms());
        property.setBathrooms(request.bathrooms());
        property.setParkingSpots(request.parkingSpots());
        property.setFloors(request.floors());
        property.setAddress(request.address());
        property.setCity(request.city());
        property.setNeighborhood(request.neighborhood());
        property.setLatitude(request.latitude());
        property.setLongitude(request.longitude());
        property.setVideoUrl(request.videoUrl());
        if (request.featured() != null) property.setFeatured(request.featured());
        return property;
    }

    private void saveNearbyPoints(Property property, List<NearbyPointDto.Request> points) {
        if (points == null || points.isEmpty()) return;
        List<NearbyPoint> entities = points.stream().map(p -> {
            NearbyPoint np = new NearbyPoint();
            np.setProperty(property);
            np.setName(p.name());
            np.setCategory(p.category());
            np.setLatitude(p.latitude());
            np.setLongitude(p.longitude());
            np.setDistanceMeters(p.distanceMeters());
            return np;
        }).collect(Collectors.toList());
        nearbyPointRepository.saveAll(entities);
    }

    private PropertyDto.Response mapToResponse(Property p) {
        List<NearbyPoint> nearby = nearbyPointRepository.findByPropertyId(p.getId());
        List<NearbyPointDto.Response> nearbyDtos = nearby.stream()
            .map(n -> new NearbyPointDto.Response(n.getId(), n.getName(), n.getCategory(),
                n.getLatitude(), n.getLongitude(), n.getDistanceMeters()))
            .collect(Collectors.toList());

        return new PropertyDto.Response(
            p.getId(), p.getTitle(), p.getDescription(), p.getPrice(),
            p.getType(), p.getStatus(), p.getAreaSqm(), p.getBedrooms(),
            p.getBathrooms(), p.getParkingSpots(), p.getFloors(),
            p.getAddress(), p.getCity(), p.getNeighborhood(),
            p.getLatitude(), p.getLongitude(),
            new ArrayList<>(p.getPhotoUrls()), p.getCoverPhotoUrl(), p.getVideoUrl(),
            p.getFeatured(), nearbyDtos, p.getCreatedAt(), p.getUpdatedAt()
        );
    }

    private PropertyDto.Summary mapToSummary(Property p) {
        return new PropertyDto.Summary(
            p.getId(), p.getTitle(), p.getPrice(), p.getType(), p.getStatus(),
            p.getAreaSqm(), p.getBedrooms(), p.getBathrooms(),
            p.getCity(), p.getNeighborhood(), p.getCoverPhotoUrl(), p.getFeatured()
        );
    }
}
