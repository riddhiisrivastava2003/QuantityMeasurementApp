package com.bridgelabz.service;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);
    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        logger.info("Service initialized with repository: {}", repository.getClass().getSimpleName());
    }

    public void saveMeasurement(QuantityMeasurementEntity entity) {
        repository.save(entity);
        logger.info("Saved measurement: {}", entity);
    }

    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return repository.findAll();
    }

    public void deleteAllMeasurements() {
        repository.deleteAll();
        logger.info("All measurements deleted.");
    }

    @Override
    public double convert(QuantityDTO dto, String targetUnit) {
        double factor = 1.0; // TODO: Proper conversion logic
        logger.info("Converting {} {} to {}", dto.getValue(), dto.getUnit(), targetUnit);
        return dto.getValue() * factor;
    }

    @Override
    public boolean compare(QuantityDTO dto1, QuantityDTO dto2) {
        logger.info("Comparing {} {} with {} {}", dto1.getValue(), dto1.getUnit(),
                dto2.getValue(), dto2.getUnit());
        // TODO: Proper comparison with unit conversion
        return dto1.getValue() == dto2.getValue();
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        logger.info("Adding {} {} + {} {}", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit());
        double result = q1.getValue() + q2.getValue();
        return new QuantityDTO(result, q1.getUnit());
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        logger.info("Subtracting {} {} - {} {}", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit());
        double result = q1.getValue() - q2.getValue();
        return new QuantityDTO(result, q1.getUnit());
    }

    @Override
    public double divide(QuantityDTO q1, QuantityDTO q2) {
        logger.info("Dividing {} {} / {} {}", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit());
        if (q2.getValue() == 0) throw new IllegalArgumentException("Cannot divide by zero");
        return q1.getValue() / q2.getValue();
    }
}