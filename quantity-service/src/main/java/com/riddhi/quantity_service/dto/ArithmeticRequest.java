package com.riddhi.quantity_service.dto;

// Generic request for two-operand arithmetic on same-type quantities
public class ArithmeticRequest {
    private double value1;
    private String unit1;
    private double value2;
    private String unit2;
    private String resultUnit;  // optional: unit to express result in (defaults to unit1)

    public double getValue1() { return value1; }
    public void setValue1(double value1) { this.value1 = value1; }
    public String getUnit1() { return unit1; }
    public void setUnit1(String unit1) { this.unit1 = unit1; }
    public double getValue2() { return value2; }
    public void setValue2(double value2) { this.value2 = value2; }
    public String getUnit2() { return unit2; }
    public void setUnit2(String unit2) { this.unit2 = unit2; }
    public String getResultUnit() { return resultUnit; }
    public void setResultUnit(String resultUnit) { this.resultUnit = resultUnit; }
}
