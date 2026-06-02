package com.marcapro.backend.repository;

import com.marcapro.backend.entity.NearbyPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NearbyPointRepository extends JpaRepository<NearbyPoint, Long> {
    List<NearbyPoint> findByPropertyId(Long propertyId);
    void deleteByPropertyId(Long propertyId);
}
