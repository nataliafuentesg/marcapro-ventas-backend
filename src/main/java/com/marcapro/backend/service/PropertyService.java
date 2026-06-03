package com.marcapro.backend.service;

import com.marcapro.backend.dto.PropertyDto;
import com.marcapro.backend.enums.PropertyStatus;
import com.marcapro.backend.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyService {
    PropertyDto.Response create(PropertyDto.Request request);
    PropertyDto.Response findById(Long id);
    Page<PropertyDto.Summary> search(PropertyType type, PropertyStatus status,
                                      BigDecimal minPrice, BigDecimal maxPrice,
                                      String city, Integer bedrooms, Pageable pageable);

    Page<PropertyDto.Summary> searchNearby(PropertyType type, PropertyStatus status,
                                            BigDecimal minPrice, BigDecimal maxPrice,
                                            Double lat, Double lng, Double radiusKm, Pageable pageable);
    List<PropertyDto.Summary> findFeatured();
    PropertyDto.Response update(Long id, PropertyDto.Request request);
    void delete(Long id);
    PropertyDto.Response addPhotos(Long id, List<MultipartFile> photos);
    PropertyDto.Response setCoverPhoto(Long id, String photoUrl);
    PropertyDto.Response removePhoto(Long id, String photoUrl);
}
