package com.riddhi.quantity_service.dto;

public class ConvertRequest {
    private double value;
    private String fromUnit;
    private String toUnit;

    // getters setters
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getFromUnit() { return fromUnit; }
    public void setFromUnit(String fromUnit) { this.fromUnit = fromUnit; }

    public String getToUnit() { return toUnit; }
    public void setToUnit(String toUnit) { this.toUnit = toUnit; }
}