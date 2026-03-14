package com.bridgelabz.app;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementDatabaseRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;
import com.bridgelabz.util.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);
    private static IQuantityMeasurementService service;

    public static void main(String[] args) {

        // Load repository type from config
        ApplicationConfig config = ApplicationConfig.getInstance();
        String repoType = config.getRepositoryType();

        IQuantityMeasurementRepository repository;

        if ("database".equalsIgnoreCase(repoType)) {
            repository = new QuantityMeasurementDatabaseRepository();
        } else {
            throw new RuntimeException("Unsupported repository type: " + repoType);
        }

        service = new QuantityMeasurementServiceImpl(repository);

        // Example measurement
        QuantityMeasurementEntity measurement = new QuantityMeasurementEntity(
                null,
                "COMPARE",
                "LENGTH",
                5.0,
                "FEET",
                60.0,
                "INCH",
                "EQUAL",
                LocalDateTime.now()
        );

        // Save measurement
        service.saveMeasurement(measurement);

        // Retrieve all measurements
        List<QuantityMeasurementEntity> measurements = service.getAllMeasurements();
        measurements.forEach(m -> logger.info("Measurement: {}", m));

        // Optional: clear all
        // service.deleteAllMeasurements();
    }
}