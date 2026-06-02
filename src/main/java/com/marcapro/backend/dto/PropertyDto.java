package com.marcapro.backend.dto;

import com.marcapro.backend.enums.PropertyStatus;
import com.marcapro.backend.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PropertyDto {

    public record Request(
        @NotBlank String title,
        String description,
        @NotNull @Positive BigDecimal price,
        @NotNull PropertyType type,
        PropertyStatus status,
        Double areaSqm,
        Integer bedrooms,
        Integer bathrooms,
        Integer parkingSpots,
        Integer floors,
        @NotBlank String address,
        String city,
        String neighborhood,
        Double latitude,
        Double longitude,
        String videoUrl,
        Boolean featured,
        List<NearbyPointDto.Request> nearbyPoints
    ) {}

    public record Response(
        Long id,
        String title,
        String description,
        BigDecimal price,
        PropertyType type,
        PropertyStatus status,
        Double areaSqm,
        Integer bedrooms,
        Integer bathrooms,
        Integer parkingSpots,
        Integer floors,
        String address,
        String city,
        String neighborhood,
        Double latitude,
        Double longitude,
        List<String> photoUrls,
        String coverPhotoUrl,
        String videoUrl,
        Boolean featured,
        List<NearbyPointDto.Response> nearbyPoints,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}

    public record Summary(
        Long id,
        String title,
        BigDecimal price,
        PropertyType type,
        PropertyStatus status,
        Double areaSqm,
        Integer bedrooms,
        Integer bathrooms,
        String city,
        String neighborhood,
        String coverPhotoUrl,
        Boolean featured
    ) {}
}
