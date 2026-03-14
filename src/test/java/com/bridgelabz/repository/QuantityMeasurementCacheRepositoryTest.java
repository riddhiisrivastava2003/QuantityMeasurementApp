package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementCacheRepositoryTest {

    @Test
    void givenEntity_WhenSaved_ShouldStoreInRepository() {

        QuantityMeasurementCacheRepository repo =
                QuantityMeasurementCacheRepository.getInstance();

        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(10,20,"ADD",30);

        repo.save(entity);

        assertFalse(repo.findAll().isEmpty());
    }
}