package com.marcapro.backend.repository;

import com.marcapro.backend.entity.Property;
import com.marcapro.backend.enums.PropertyStatus;
import com.marcapro.backend.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    Page<Property> findByStatus(PropertyStatus status, Pageable pageable);

    List<Property> findByFeaturedTrueAndStatus(PropertyStatus status);

    @Query(value = """
        SELECT * FROM properties p
        WHERE p.latitude IS NOT NULL AND p.longitude IS NOT NULL
          AND (:type IS NULL OR p.type = :type)
          AND (:status IS NULL OR p.status = :status)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (6371 * acos(LEAST(1.0, cos(radians(:lat)) * cos(radians(p.latitude))
               * cos(radians(p.longitude) - radians(:lng))
               + sin(radians(:lat)) * sin(radians(p.latitude))))) <= :radiusKm
        ORDER BY (6371 * acos(LEAST(1.0, cos(radians(:lat)) * cos(radians(p.latitude))
               * cos(radians(p.longitude) - radians(:lng))
               + sin(radians(:lat)) * sin(radians(p.latitude))))) ASC
    """, countQuery = """
        SELECT COUNT(*) FROM properties p
        WHERE p.latitude IS NOT NULL AND p.longitude IS NOT NULL
          AND (:type IS NULL OR p.type = :type)
          AND (:status IS NULL OR p.status = :status)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (6371 * acos(LEAST(1.0, cos(radians(:lat)) * cos(radians(p.latitude))
               * cos(radians(p.longitude) - radians(:lng))
               + sin(radians(:lat)) * sin(radians(p.latitude))))) <= :radiusKm
    """, nativeQuery = true)
    Page<Property> searchNearby(
        String type, String status,
        BigDecimal minPrice, BigDecimal maxPrice,
        Double lat, Double lng, Double radiusKm,
        Pageable pageable
    );

    @Query(value = """
        SELECT * FROM properties p
        WHERE (:type IS NULL OR p.type = :type)
          AND (:status IS NULL OR p.status = :status)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:city IS NULL OR LOWER(p.city::text) LIKE LOWER(CONCAT('%', :city, '%')))
          AND (:bedrooms IS NULL OR p.bedrooms >= :bedrooms)
    """, countQuery = """
        SELECT COUNT(*) FROM properties p
        WHERE (:type IS NULL OR p.type = :type)
          AND (:status IS NULL OR p.status = :status)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:city IS NULL OR LOWER(p.city::text) LIKE LOWER(CONCAT('%', :city, '%')))
          AND (:bedrooms IS NULL OR p.bedrooms >= :bedrooms)
    """, nativeQuery = true)
    Page<Property> search(
        String type,
        String status,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String city,
        Integer bedrooms,
        Pageable pageable
    );
}
