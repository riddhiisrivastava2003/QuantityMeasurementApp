package com.bridgelabz.service;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.dto.QuantityDTO;

import java.util.*;

public interface IQuantityMeasurementService {

    QuantityDTO convert(QuantityDTO input, String targetUnit);

    boolean compare(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);

    double divide(QuantityDTO q1, QuantityDTO q2);
    double convert(double value, String fromUnit, String toUnit);
    List<QuantityMeasurementEntity> getAll();
    QuantityMeasurementEntity getById(Long id);
    void delete(Long id);
}