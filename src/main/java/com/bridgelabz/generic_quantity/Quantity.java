package com.bridgelabz.generic_quantity;

import java.util.Objects;

public final class Quantity<U extends IMeasurable> {

    private final double value;

    private final U unit;

    public Quantity(double value, U unit) {


        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");

        this.value = value;

        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> convertTo(U targetUnit) {

        if (!unit.getClass().equals(targetUnit.getClass())) throw new IllegalArgumentException("Cannot convert between different measurement categories");

        double baseValue = unit.convertToBaseUnit(value);

        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(round(convertedValue), targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        if (!unit.getClass().equals(other.unit.getClass())) throw new IllegalArgumentException("Cannot add different measurement categories");

        double base1 = unit.convertToBaseUnit(value);


        double base2 = other.unit.convertToBaseUnit(other.value);

        double sumBase = base1 + base2;


        double finalValue = targetUnit.convertFromBaseUnit(sumBase);

        return new Quantity<>(round(finalValue), targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> that)) return false;

        if (!unit.getClass().equals(that.unit.getClass())) return false;

        double base1 = unit.convertToBaseUnit(value);


        double base2 = that.unit.convertToBaseUnit(that.value);

        return Double.compare(round(base1), round(base2)) == 0;
    }

    @Override
    public int hashCode() {

        double base = round(unit.convertToBaseUnit(value));

        return Objects.hash(base, unit.getClass());
    }

    @Override
    public String toString() {

        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }

    private double round(double value) {

        return Math.round(value * 100.0) / 100.0;
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Quantity cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Cross-category operation not allowed");

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double baseResult = base1 - base2;

        double finalValue = targetUnit.convertFromBaseUnit(baseResult);

        finalValue = Math.round(finalValue * 100.0) / 100.0;

        return new Quantity<>(finalValue, targetUnit);
    }

    public double divide(Quantity<U> other) {

        if (other == null)
            throw new IllegalArgumentException("Quantity cannot be null");

        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Cross-category operation not allowed");

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        if (base2 == 0)
            throw new ArithmeticException("Division by zero");

        return base1 / base2;
    }
}