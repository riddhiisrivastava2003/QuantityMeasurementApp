package com.bridgelabz.dto;

public class ConvertRequest {

    private double value;
    private String fromUnit;
    private String toUnit;

    // Getter
    public double getValue() {
        return value;
    }

    public String getFromUnit() {
        return fromUnit;
    }

    public String getToUnit() {
        return toUnit;
    }

    // Setters (IMPORTANT for POST request)
    public void setValue(double value) {
        this.value = value;
    }

    public void setFromUnit(String fromUnit) {
        this.fromUnit = fromUnit;
    }

    public void setToUnit(String toUnit) {
        this.toUnit = toUnit;
    }
}