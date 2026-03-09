package com.bridgelabz.unit_to_unit_conversion;



import java.util.Objects;


public final class QuantityLength {

    private final double value;
    private final LengthUnit unit;
    private static final double EPSILON = 1e-6;


    public QuantityLength(double value, LengthUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {

        double convertedValue = convert(this.value, this.unit, targetUnit);
        return new QuantityLength(convertedValue, targetUnit);
    }


    public static double convert(double value,
                                 LengthUnit source,
                                 LengthUnit target) {

        validate(value, source, target);

        if (source == target) {
            return value;
        }


        double valueInFeet = value * source.getConversionFactor();


        double result = valueInFeet / target.getConversionFactor();

        return round(result);
    }

    private static void validate(double value,
                                 LengthUnit source,
                                 LengthUnit target) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }

        if (source == null || target == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
    }

    private static double round(double value) {
        return Math.round(value * 1000000.0) / 1000000.0;
    }

    private double toBaseUnit() {
        return this.value * this.unit.getConversionFactor();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(round(toBaseUnit()));
    }


    @Override
    public String toString() {
        return value + " " + unit;
    }
}