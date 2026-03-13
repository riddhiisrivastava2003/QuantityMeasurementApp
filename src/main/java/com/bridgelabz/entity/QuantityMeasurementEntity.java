package com.bridgelabz.entity;

import java.io.Serializable;

public class QuantityMeasurementEntity implements Serializable {

    private double operand1;
    private double operand2;
    private String operation;
    private double result;

    public QuantityMeasurementEntity(double operand1, double operand2, String operation, double result) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operation = operation;
        this.result = result;
    }

    public double getResult() {
        return result;
    }
}