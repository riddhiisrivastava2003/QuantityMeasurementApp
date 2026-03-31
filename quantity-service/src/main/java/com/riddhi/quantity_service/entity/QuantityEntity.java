package com.riddhi.quantity_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quantity")
public class QuantityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double value;
    private String unit;

    // getters setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}