package com.riddhi.quantity_service.service;

import com.riddhi.quantity_service.dto.QuantityDTO;
import com.riddhi.quantity_service.entity.QuantityOperation;
import com.riddhi.quantity_service.exceptions.ResourceNotFoundException;
import com.riddhi.quantity_service.repository.QuantityRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityService {

    private final QuantityRepository repository;

    public QuantityService(QuantityRepository repository) {
        this.repository = repository;
    }

    // ── Existing DTO-based operations ──────────────────────────────────────
    public double add(QuantityDTO q1, QuantityDTO q2) {
        double v1 = toBase(q1.getValue(), q1.getUnit());
        double v2 = toBase(q2.getValue(), q2.getUnit());
        double result = fromBase(v1 + v2, q1.getUnit());
        saveToDB("ADD", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit(), result);
        return result;
    }

    public double subtract(QuantityDTO q1, QuantityDTO q2) {
        double v1 = toBase(q1.getValue(), q1.getUnit());
        double v2 = toBase(q2.getValue(), q2.getUnit());
        double result = fromBase(v1 - v2, q1.getUnit());
        saveToDB("SUBTRACT", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit(), result);
        return result;
    }

    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
        double v1 = toBase(q1.getValue(), q1.getUnit());
        double v2 = toBase(q2.getValue(), q2.getUnit());
        boolean result = Math.abs(v1 - v2) < 0.0001;
        saveToDB("COMPARE", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit(), result ? 1.0 : 0.0);
        return result;
    }

    public double divide(QuantityDTO q1, QuantityDTO q2) {
        double v1 = toBase(q1.getValue(), q1.getUnit());
        double v2 = toBase(q2.getValue(), q2.getUnit());
        if (v2 == 0) throw new RuntimeException("Cannot divide by zero");
        double result = v1 / v2;
        saveToDB("DIVIDE", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit(), result);
        return result;
    }

    // ── New arithmetic operations (value + unit pairs, result in resultUnit) ─
    // ADD two quantities (can be different units, same category)
    public double addArithmetic(double val1, String unit1, double val2, String unit2, String resultUnit) {
        double base = toBase(val1, unit1) + toBase(val2, unit2);
        String outUnit = (resultUnit != null && !resultUnit.isBlank()) ? resultUnit : unit1;
        double result = fromBase(base, outUnit);
        saveToDB("ADD", val1, unit1, val2, unit2, result);
        return result;
    }

    // SUBTRACT
    public double subtractArithmetic(double val1, String unit1, double val2, String unit2, String resultUnit) {
        double base = toBase(val1, unit1) - toBase(val2, unit2);
        String outUnit = (resultUnit != null && !resultUnit.isBlank()) ? resultUnit : unit1;
        double result = fromBase(base, outUnit);
        saveToDB("SUBTRACT", val1, unit1, val2, unit2, result);
        return result;
    }

    // MULTIPLY (scalar multiply — value2/unit2 treated as scalar)
    public double multiply(double val1, String unit1, double scalar) {
        double result = val1 * scalar;
        saveToDB("MULTIPLY", val1, unit1, scalar, "SCALAR", result);
        return result;
    }

    // DIVIDE (scalar divide)
    public double divideScalar(double val1, String unit1, double scalar) {
        if (scalar == 0) throw new RuntimeException("Cannot divide by zero");
        double result = val1 / scalar;
        saveToDB("DIVIDE", val1, unit1, scalar, "SCALAR", result);
        return result;
    }

    // COMPARE — returns "GREATER", "LESS", or "EQUAL"
    public String compareArithmetic(double val1, String unit1, double val2, String unit2) {
        double b1 = toBase(val1, unit1);
        double b2 = toBase(val2, unit2);
        String result;
        if (Math.abs(b1 - b2) < 0.0001) result = "EQUAL";
        else if (b1 > b2)               result = "GREATER";
        else                             result = "LESS";
        saveToDB("COMPARE", val1, unit1, val2, unit2, b1 - b2);
        return result;
    }

    // ── CONVERT ───────────────────────────────────────────────────────────
    @Cacheable(value = "conversions", key = "#value + '-' + #from + '-' + #to")
    public double convert(double value, String from, String to) {
        System.out.println("Cache miss — computing: " + value + " " + from + " -> " + to);
        return fromBase(toBase(value, from), to);
    }

    public void saveConvertOperation(double value, String fromUnit, String toUnit, double result) {
        saveToDB("CONVERT", value, fromUnit, 0, toUnit, result);
    }

    // ── CRUD ─────────────────────────────────────────────────────────────
    public List<QuantityOperation> getAll() { return repository.findAll(); }

    public QuantityOperation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + id));
    }

    public String deleteById(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Not found: " + id);
        repository.deleteById(id);
        return "Deleted successfully";
    }

    // ── internal helpers ─────────────────────────────────────────────────
    private void saveToDB(String op, double v1, String u1, double v2, String u2, double result) {
        QuantityOperation o = new QuantityOperation();
        o.setOperation(op);
        o.setMeasurementType(getMeasurementType(u1));
        o.setValue1(v1); o.setUnit1(u1);
        o.setValue2(v2); o.setUnit2(u2);
        o.setResult(result);
        repository.save(o);
    }

    // Convert value to a universal base unit for its category
    public double toBase(double value, String unit) {
        return switch (unit.toUpperCase().trim()) {
            // LENGTH → base: INCH
            case "FEET","FOOT","FT"                 -> value * 12.0;
            case "INCHES","INCH","IN"               -> value;
            case "YARD","YARDS","YD"                -> value * 36.0;
            case "METER","METERS","M"               -> value * 39.3701;
            case "CENTIMETER","CENTIMETERS","CM"    -> value * 0.393701;
            case "KILOMETER","KILOMETERS","KM"      -> value * 39370.1;
            case "MILLIMETER","MILLIMETERS","MM"    -> value * 0.0393701;
            case "MILE","MILES","MI"                -> value * 63360.0;
            // WEIGHT → base: GRAM
            case "GRAM","GRAMS","G"                 -> value;
            case "KILOGRAM","KILOGRAMS","KG"        -> value * 1000.0;
            case "POUND","POUNDS","LB","LBS"        -> value * 453.592;
            case "OUNCE","OUNCES","OZ"              -> value * 28.3495;
            case "TON","TONS"                       -> value * 1_000_000.0;
            // TEMPERATURE → base: CELSIUS
            case "CELSIUS","C"                      -> value;
            case "FAHRENHEIT","F"                   -> (value - 32.0) * 5.0 / 9.0;
            case "KELVIN","K"                       -> value - 273.15;
            // VOLUME → base: MILLILITER
            case "MILLILITER","MILLILITERS","ML"    -> value;
            case "LITER","LITERS","L"               -> value * 1000.0;
            case "GALLON","GALLONS","GAL"           -> value * 3785.41;
            case "CUP","CUPS"                       -> value * 236.588;
            // SCALAR (for multiply/divide)
            case "SCALAR"                           -> value;
            default -> throw new RuntimeException("Unknown unit: " + unit);
        };
    }

    public double fromBase(double value, String unit) {
        return switch (unit.toUpperCase().trim()) {
            case "FEET","FOOT","FT"                 -> value / 12.0;
            case "INCHES","INCH","IN"               -> value;
            case "YARD","YARDS","YD"                -> value / 36.0;
            case "METER","METERS","M"               -> value / 39.3701;
            case "CENTIMETER","CENTIMETERS","CM"    -> value / 0.393701;
            case "KILOMETER","KILOMETERS","KM"      -> value / 39370.1;
            case "MILLIMETER","MILLIMETERS","MM"    -> value / 0.0393701;
            case "MILE","MILES","MI"                -> value / 63360.0;
            case "GRAM","GRAMS","G"                 -> value;
            case "KILOGRAM","KILOGRAMS","KG"        -> value / 1000.0;
            case "POUND","POUNDS","LB","LBS"        -> value / 453.592;
            case "OUNCE","OUNCES","OZ"              -> value / 28.3495;
            case "TON","TONS"                       -> value / 1_000_000.0;
            case "CELSIUS","C"                      -> value;
            case "FAHRENHEIT","F"                   -> value * 9.0 / 5.0 + 32.0;
            case "KELVIN","K"                       -> value + 273.15;
            case "MILLILITER","MILLILITERS","ML"    -> value;
            case "LITER","LITERS","L"               -> value / 1000.0;
            case "GALLON","GALLONS","GAL"           -> value / 3785.41;
            case "CUP","CUPS"                       -> value / 236.588;
            case "SCALAR"                           -> value;
            default -> throw new RuntimeException("Unknown unit: " + unit);
        };
    }

    public String getMeasurementType(String unit) {
        String u = unit.toUpperCase().trim();
        return switch (u) {
            case "FEET","FOOT","FT","INCHES","INCH","IN","YARD","YARDS","YD",
                 "METER","METERS","M","CENTIMETER","CENTIMETERS","CM",
                 "KILOMETER","KILOMETERS","KM","MILLIMETER","MILLIMETERS","MM",
                 "MILE","MILES","MI" -> "LENGTH";
            case "GRAM","GRAMS","G","KILOGRAM","KILOGRAMS","KG",
                 "POUND","POUNDS","LB","LBS","OUNCE","OUNCES","OZ","TON","TONS" -> "WEIGHT";
            case "CELSIUS","C","FAHRENHEIT","F","KELVIN","K" -> "TEMPERATURE";
            case "MILLILITER","MILLILITERS","ML","LITER","LITERS","L",
                 "GALLON","GALLONS","GAL","CUP","CUPS" -> "VOLUME";
            default -> "UNKNOWN";
        };
    }
}
