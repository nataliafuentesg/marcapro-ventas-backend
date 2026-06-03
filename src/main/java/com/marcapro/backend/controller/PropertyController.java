package com.marcapro.backend.controller;

import com.marcapro.backend.dto.PropertyDto;
import com.marcapro.backend.enums.PropertyStatus;
import com.marcapro.backend.enums.PropertyType;
import com.marcapro.backend.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/nearby")
    public ResponseEntity<Page<PropertyDto.Summary>> searchNearby(
        @RequestParam Double lat,
        @RequestParam Double lng,
        @RequestParam(defaultValue = "10") Double radiusKm,
        @RequestParam(required = false) PropertyType type,
        @RequestParam(required = false) PropertyStatus status,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @PageableDefault(size = 12) Pageable pageable
    ) {
        return ResponseEntity.ok(propertyService.searchNearby(type, status, minPrice, maxPrice, lat, lng, radiusKm, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<PropertyDto.Summary>> search(
        @RequestParam(required = false) PropertyType type,
        @RequestParam(required = false) PropertyStatus status,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) Integer bedrooms,
        @PageableDefault(size = 12, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(propertyService.search(type, status, minPrice, maxPrice, city, bedrooms, pageable));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<PropertyDto.Summary>> featured() {
        return ResponseEntity.ok(propertyService.findFeatured());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertyDto.Response> create(@Valid @RequestBody PropertyDto.Request request) {
        PropertyDto.Response response = propertyService.create(request);
        return ResponseEntity.created(URI.create("/api/properties/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertyDto.Response> update(@PathVariable Long id,
                                                        @Valid @RequestBody PropertyDto.Request request) {
        return ResponseEntity.ok(propertyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/photos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertyDto.Response> addPhotos(@PathVariable Long id,
                                                           @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(propertyService.addPhotos(id, files));
    }

    @PutMapping("/{id}/cover")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertyDto.Response> setCover(@PathVariable Long id,
                                                          @RequestParam String photoUrl) {
        return ResponseEntity.ok(propertyService.setCoverPhoto(id, photoUrl));
    }

    @DeleteMapping("/{id}/photos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertyDto.Response> removePhoto(@PathVariable Long id,
                                                             @RequestParam String photoUrl) {
        return ResponseEntity.ok(propertyService.removePhoto(id, photoUrl));
    }
}
