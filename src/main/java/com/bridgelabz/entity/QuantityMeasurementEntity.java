package com.bridgelabz.entity;

import java.time.LocalDateTime;

public class QuantityMeasurementEntity {

    private Long id; // database primary key
    private String operation; // e.g., "Addition", "Comparison"
    private String measurementType; // e.g., "Length", "Weight"
    private double value1;
    private String unit1;
    private double value2;
    private String unit2;
    private String result; // result as string
    private LocalDateTime createdAt; // timestamp for audit

    // Default constructor
    public QuantityMeasurementEntity() {
        this.createdAt = LocalDateTime.now();
    }

    // Parameterized constructor
    public QuantityMeasurementEntity(String operation, String measurementType, double value1, String unit1,
                                     double value2, String unit2, String result) {
        this.operation = operation;
        this.measurementType = measurementType;
        this.value1 = value1;
        this.unit1 = unit1;
        this.value2 = value2;
        this.unit2 = unit2;
        this.result = result;
        this.createdAt = LocalDateTime.now();
    }

    // Full constructor including ID (for retrieving from DB)
    public QuantityMeasurementEntity(Long id, String operation, String measurementType, double value1, String unit1,
                                     double value2, String unit2, String result, LocalDateTime createdAt) {
        this.id = id;
        this.operation = operation;
        this.measurementType = measurementType;
        this.value1 = value1;
        this.unit1 = unit1;
        this.value2 = value2;
        this.unit2 = unit2;
        this.result = result;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

    public String getUnit1() {
        return unit1;
    }

    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
    }

    public String getUnit2() {
        return unit2;
    }

    public void setUnit2(String unit2) {
        this.unit2 = unit2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "QuantityMeasurementEntity{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", measurementType='" + measurementType + '\'' +
                ", value1=" + value1 +
                ", unit1='" + unit1 + '\'' +
                ", value2=" + value2 +
                ", unit2='" + unit2 + '\'' +
                ", result='" + result + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}