package com.marcapro.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nearby_points")
@Getter
@Setter
public class NearbyPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private String name;
    private String category;
    private Double latitude;
    private Double longitude;
    private Double distanceMeters;
}
