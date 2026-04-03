package com.riddhi.quantity_service.service;

import com.riddhi.quantity_service.dto.QuantityDTO;
import com.riddhi.quantity_service.entity.QuantityOperation;
import com.riddhi.quantity_service.exceptions.ResourceNotFoundException;
import com.riddhi.quantity_service.repository.QuantityRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityService {

    private final QuantityRepository repository;

    public QuantityService(QuantityRepository repository) {
        this.repository = repository;
    }

    public double add(QuantityDTO q1, QuantityDTO q2) {
        double v1 = convertToBase(q1.getValue(), q1.getUnit());
        double v2 = convertToBase(q2.getValue(), q2.getUnit());
        double result = convertFromBase(v1 + v2, "INCHES");
        saveToDB("ADD", q1, q2, result);
        return result;
    }

    public double subtract(QuantityDTO q1, QuantityDTO q2) {
        double v1 = convertToBase(q1.getValue(), q1.getUnit());
        double v2 = convertToBase(q2.getValue(), q2.getUnit());
        double result = v1 - v2;
        saveToDB("SUBTRACT", q1, q2, result);
        return result;
    }

    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
        double v1 = convertToBase(q1.getValue(), q1.getUnit());
        double v2 = convertToBase(q2.getValue(), q2.getUnit());
        boolean result = Math.abs(v1 - v2) < 0.0001;
        saveToDB("COMPARE", q1, q2, result ? 1.0 : 0.0);
        return result;
    }

    public double divide(QuantityDTO q1, QuantityDTO q2) {
        double v1 = convertToBase(q1.getValue(), q1.getUnit());
        double v2 = convertToBase(q2.getValue(), q2.getUnit());
        if (v2 == 0) throw new RuntimeException("Cannot divide by zero");
        double result = v1 / v2;
        saveToDB("DIVIDE", q1, q2, result);
        return result;
    }

    // ✅ Convert with Spring's built-in cache (no Redis needed)
    @Cacheable(value = "conversions", key = "#value + '-' + #from + '-' + #to")
    public double convert(double value, String from, String to) {
        System.out.println("Cache miss - computing conversion: " + value + " " + from + " -> " + to);
        from = from.toUpperCase().trim();
        to = to.toUpperCase().trim();

        double valueInBase = convertToBase(value, from);
        return convertFromBase(valueInBase, to);
    }

    // ✅ Save convert operation to DB
    public void saveConvertOperation(double value, String fromUnit, String toUnit, double result) {
        QuantityOperation op = new QuantityOperation();
        op.setOperation("CONVERT");
        op.setMeasurementType(getMeasurementType(fromUnit));
        op.setValue1(value);
        op.setUnit1(fromUnit);
        op.setValue2(0.0);
        op.setUnit2(toUnit);
        op.setResult(result);
        repository.save(op);
    }

    // Convert value to base unit (INCH for length, GRAM for weight, CELSIUS for temp)
    private double convertToBase(double value, String unit) {
        return switch (unit.toUpperCase().trim()) {
            // LENGTH (base: INCH)
            case "FEET", "FOOT", "FT" -> value * 12.0;
            case "INCHES", "INCH", "IN" -> value;
            case "YARD", "YARDS", "YD" -> value * 36.0;
            case "METER", "METERS", "M" -> value * 39.3701;
            case "CENTIMETER", "CENTIMETERS", "CM" -> value * 0.393701;
            case "KILOMETER", "KILOMETERS", "KM" -> value * 39370.1;
            case "MILLIMETER", "MILLIMETERS", "MM" -> value * 0.0393701;
            case "MILE", "MILES", "MI" -> value * 63360.0;

            // WEIGHT (base: GRAM)
            case "KILOGRAM", "KILOGRAMS", "KG" -> value * 1000.0;
            case "GRAM", "GRAMS", "G" -> value;
            case "POUND", "POUNDS", "LB", "LBS" -> value * 453.592;
            case "OUNCE", "OUNCES", "OZ" -> value * 28.3495;
            case "TON", "TONS" -> value * 1000000.0;

            // TEMPERATURE (base: CELSIUS) - special case, handled separately
            case "CELSIUS", "C" -> value;
            case "FAHRENHEIT", "F" -> (value - 32.0) * 5.0 / 9.0;
            case "KELVIN", "K" -> value - 273.15;

            // VOLUME (base: MILLILITER)
            case "LITER", "LITERS", "L" -> value * 1000.0;
            case "MILLILITER", "MILLILITERS", "ML" -> value;
            case "GALLON", "GALLONS", "GAL" -> value * 3785.41;
            case "CUP", "CUPS" -> value * 236.588;

            default -> throw new RuntimeException("Unknown unit: " + unit);
        };
    }

    private double convertFromBase(double value, String unit) {
        return switch (unit.toUpperCase().trim()) {
            // LENGTH
            case "FEET", "FOOT", "FT" -> value / 12.0;
            case "INCHES", "INCH", "IN" -> value;
            case "YARD", "YARDS", "YD" -> value / 36.0;
            case "METER", "METERS", "M" -> value / 39.3701;
            case "CENTIMETER", "CENTIMETERS", "CM" -> value / 0.393701;
            case "KILOMETER", "KILOMETERS", "KM" -> value / 39370.1;
            case "MILLIMETER", "MILLIMETERS", "MM" -> value / 0.0393701;
            case "MILE", "MILES", "MI" -> value / 63360.0;

            // WEIGHT
            case "KILOGRAM", "KILOGRAMS", "KG" -> value / 1000.0;
            case "GRAM", "GRAMS", "G" -> value;
            case "POUND", "POUNDS", "LB", "LBS" -> value / 453.592;
            case "OUNCE", "OUNCES", "OZ" -> value / 28.3495;
            case "TON", "TONS" -> value / 1000000.0;

            // TEMPERATURE
            case "CELSIUS", "C" -> value;
            case "FAHRENHEIT", "F" -> value * 9.0 / 5.0 + 32.0;
            case "KELVIN", "K" -> value + 273.15;

            // VOLUME
            case "LITER", "LITERS", "L" -> value / 1000.0;
            case "MILLILITER", "MILLILITERS", "ML" -> value;
            case "GALLON", "GALLONS", "GAL" -> value / 3785.41;
            case "CUP", "CUPS" -> value / 236.588;

            default -> throw new RuntimeException("Unknown unit: " + unit);
        };
    }

    private String getMeasurementType(String unit) {
        String u = unit.toUpperCase().trim();
        if (List.of("FEET","FOOT","FT","INCHES","INCH","IN","YARD","YARDS","YD","METER","METERS","M","CM","KM","MM","MILE","MILES","MI").contains(u))
            return "LENGTH";
        if (List.of("KILOGRAM","KILOGRAMS","KG","GRAM","GRAMS","G","POUND","POUNDS","LB","LBS","OUNCE","OUNCES","OZ","TON","TONS").contains(u))
            return "WEIGHT";
        if (List.of("CELSIUS","C","FAHRENHEIT","F","KELVIN","K").contains(u))
            return "TEMPERATURE";
        if (List.of("LITER","LITERS","L","MILLILITER","MILLILITERS","ML","GALLON","GALLONS","GAL","CUP","CUPS").contains(u))
            return "VOLUME";
        return "UNKNOWN";
    }

    private void saveToDB(String operation, QuantityDTO q1, QuantityDTO q2, double result) {
        QuantityOperation op = new QuantityOperation();
        op.setOperation(operation);
        op.setMeasurementType(getMeasurementType(q1.getUnit()));
        op.setValue1(q1.getValue());
        op.setUnit1(q1.getUnit());
        op.setValue2(q2.getValue());
        op.setUnit2(q2.getUnit());
        op.setResult(result);
        repository.save(op);
    }

    public List<QuantityOperation> getAll() {
        return repository.findAll();
    }

    public QuantityOperation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data not found with id: " + id));
    }

    public String deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Data not found with id: " + id);
        }
        repository.deleteById(id);
        return "Deleted successfully ✅";
    }
}
