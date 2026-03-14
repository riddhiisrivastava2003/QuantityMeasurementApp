package com.bridgelabz.unit_addition;

public enum LengthUnit {

    FEET(1.0),

    INCHES(1.0 / 12.0),

    YARDS(3.0),

    CENTIMETERS(0.0328084);  // 1 cm = 0.0328084 feet

    private final double conversionFactorToFeet;

    LengthUnit(double conversionFactorToFeet) {
        this.conversionFactorToFeet = conversionFactorToFeet;
    }

    public double toFeet(double value) {
        return value * conversionFactorToFeet;
    }

    public double fromFeet(double feetValue) {
        return feetValue / conversionFactorToFeet;
    }
}