package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {
    void deleteById(Long id);
    // List<QuantityMeasurement> findByUnitType(String unitType);
}