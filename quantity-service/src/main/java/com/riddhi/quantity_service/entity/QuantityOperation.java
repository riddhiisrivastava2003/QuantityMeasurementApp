package com.riddhi.quantity_service.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_operations")
public class QuantityOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;
    private String measurementType;

    private double value1;
    private String unit1;

    private double value2;
    private String unit2;

    private double result;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters setters
    public Long getId() { return id; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getMeasurementType() { return measurementType; }
    public void setMeasurementType(String measurementType) { this.measurementType = measurementType; }

    public double getValue1() { return value1; }
    public void setValue1(double value1) { this.value1 = value1; }

    public String getUnit1() { return unit1; }
    public void setUnit1(String unit1) { this.unit1 = unit1; }

    public double getValue2() { return value2; }
    public void setValue2(double value2) { this.value2 = value2; }

    public String getUnit2() { return unit2; }
    public void setUnit2(String unit2) { this.unit2 = unit2; }

    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}