package com.bridgelabz.controller;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.service.IQuantityMeasurementService;

public class QuantityMeasurementController {

    private IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    public boolean performComparison(QuantityDTO q1, QuantityDTO q2) {
        return service.compare(q1,q2);
    }

    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2){
        return service.add(q1,q2);
    }
}