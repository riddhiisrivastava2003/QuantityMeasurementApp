package com.bridgelabz.app;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        IQuantityMeasurementRepository repository =
                QuantityMeasurementCacheRepository.getInstance();

        IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);

        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);

        QuantityDTO q1 = new QuantityDTO(10,"FEET");
        QuantityDTO q2 = new QuantityDTO(10,"FEET");

        boolean result = controller.performComparison(q1,q2);

        System.out.println("Comparison Result: " + result);
    }
}