package com.riddhi.quantity_service.dto;

public class Quantity {

    private double value;
    private String unit;

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}