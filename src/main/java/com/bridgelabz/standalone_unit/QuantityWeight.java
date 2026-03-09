package com.bridgelabz.standalone_unit;

import java.util.Objects;


public final class QuantityWeight {


    private static final double EPSILON = 1e-6;

    private final double value;

    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {


        if (unit == null)

            throw new IllegalArgumentException("Unit cannot be null");

        if (Double.isNaN(value) || Double.isInfinite(value))


            throw new IllegalArgumentException("Invalid numeric value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {

        return value;
    }

    public WeightUnit getUnit() {

        return unit;
    }

    // Convert to target unit
    public QuantityWeight convertTo(WeightUnit targetUnit) {



        if (targetUnit == null)

            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue = unit.convertToBaseUnit(value);

        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new QuantityWeight(convertedValue, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other) {

        if (other == null)
            throw new IllegalArgumentException("Other quantity cannot be null");

        double baseSum = this.toBaseUnit() + other.toBaseUnit();

        double finalValue = this.unit.convertFromBaseUnit(baseSum);

        return new QuantityWeight(finalValue, this.unit);
    }


    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Other quantity cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseSum = this.toBaseUnit() + other.toBaseUnit();

        double finalValue = targetUnit.convertFromBaseUnit(baseSum);

        return new QuantityWeight(finalValue, targetUnit);
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        QuantityWeight other = (QuantityWeight) obj;

        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        double baseValue = toBaseUnit();
        long rounded = Math.round(baseValue / EPSILON);
        return Objects.hash(rounded);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}