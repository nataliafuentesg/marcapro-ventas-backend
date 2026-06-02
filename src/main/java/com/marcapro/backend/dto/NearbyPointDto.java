package com.marcapro.backend.dto;

public class NearbyPointDto {

    public record Request(
        String name,
        String category,
        Double latitude,
        Double longitude,
        Double distanceMeters
    ) {}

    public record Response(
        Long id,
        String name,
        String category,
        Double latitude,
        Double longitude,
        Double distanceMeters
    ) {}
}
