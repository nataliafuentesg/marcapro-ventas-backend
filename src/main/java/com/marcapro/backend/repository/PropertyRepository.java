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

    @Query("""
        SELECT p FROM Property p
        WHERE (:type IS NULL OR p.type = :type)
          AND (:status IS NULL OR p.status = :status)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:city IS NULL OR LOWER(CAST(p.city AS string)) LIKE LOWER(CONCAT('%', :city, '%')))
          AND (:bedrooms IS NULL OR p.bedrooms >= :bedrooms)
    """)
    Page<Property> search(
        PropertyType type,
        PropertyStatus status,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String city,
        Integer bedrooms,
        Pageable pageable
    );
}
