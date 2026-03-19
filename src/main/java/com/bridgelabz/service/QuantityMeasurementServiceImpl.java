package com.bridgelabz.service;

import com.bridgelabz.dto.QuantityDTO;
import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.exception.ResourceNotFoundException;
import com.bridgelabz.repository.QuantityMeasurementRepository;
import com.bridgelabz.unit.generic_quantity.IMeasurable;
import com.bridgelabz.unit.generic_quantity.UnitFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;

    // 🔥 COMMON VALIDATION
    private void validateSameType(IMeasurable u1, IMeasurable u2) {
        if (!u1.getMeasurementType().equals(u2.getMeasurementType())) {
            throw new QuantityMeasurementException("Different measurement types");
        }
    }

    private void validateArithmetic(IMeasurable u1, IMeasurable u2) {
        if (!u1.supportsArithmetic() || !u2.supportsArithmetic()) {
            throw new QuantityMeasurementException("Operation not allowed for this unit");
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO input, String targetUnit) {
        return null;
    }

    // COMPARE (ONE ONLY)
    @Override
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {

        IMeasurable unit1 = UnitFactory.getUnit(q1.getUnit());
        IMeasurable unit2 = UnitFactory.getUnit(q2.getUnit());

        validateSameType(unit1, unit2);

        double base1 = unit1.convertToBaseUnit(q1.getValue());
        double base2 = unit2.convertToBaseUnit(q2.getValue());

        boolean result = Double.compare(base1, base2) == 0;

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setOperation("COMPARE");
        entity.setMeasurementType(unit1.getMeasurementType());
        entity.setValue1(q1.getValue());
        entity.setValue2(q2.getValue());
        entity.setResult(result);

        repository.save(entity);

        return result;
    }

    //  ADD
    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {

        IMeasurable unit1 = UnitFactory.getUnit(q1.getUnit());
        IMeasurable unit2 = UnitFactory.getUnit(q2.getUnit());

        validateSameType(unit1, unit2);
        validateArithmetic(unit1, unit2);

        double base1 = unit1.convertToBaseUnit(q1.getValue());
        double base2 = unit2.convertToBaseUnit(q2.getValue());

        double sumBase = base1 + base2;

        double result = unit1.convertFromBaseUnit(sumBase);

        repository.save(new QuantityMeasurementEntity());

        return new QuantityDTO(result, q1.getUnit());
    }

    // SUBTRACT
    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {

        IMeasurable unit1 = UnitFactory.getUnit(q1.getUnit());
        IMeasurable unit2 = UnitFactory.getUnit(q2.getUnit());

        validateSameType(unit1, unit2);
        validateArithmetic(unit1, unit2);

        double base1 = unit1.convertToBaseUnit(q1.getValue());
        double base2 = unit2.convertToBaseUnit(q2.getValue());

        double resultBase = base1 - base2;

        double result = unit1.convertFromBaseUnit(resultBase);

        return new QuantityDTO(result, q1.getUnit());
    }

    // DIVIDE
    @Override
    public double divide(QuantityDTO q1, QuantityDTO q2) {

        if (q2.getValue() == 0) {
            throw new QuantityMeasurementException("Cannot divide by zero");
        }

        return q1.getValue() / q2.getValue();
    }

    // CONVERT (MAIN FOR TEMPERATURE 🔥)
    @Override
    public double convert(double value, String fromUnit, String toUnit) {

        IMeasurable from = UnitFactory.getUnit(fromUnit);
        IMeasurable to = UnitFactory.getUnit(toUnit);

        validateSameType(from, to);

        double base = from.convertToBaseUnit(value);
        return to.convertFromBaseUnit(base);
    }

    // GET ALL
    @Override
    public List<QuantityMeasurementEntity> getAll() {
        return repository.findAll();
    }

    // GET BY ID
    @Override
    public QuantityMeasurementEntity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data not found with id: " + id));
    }

    // DELETE
    @Override
    public void delete(Long id) {

        QuantityMeasurementEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete. Data not found with id: " + id));

        repository.delete(entity);
    }
}