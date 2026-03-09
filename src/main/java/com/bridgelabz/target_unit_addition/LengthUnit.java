package com.bridgelabz.target_unit_addition;

public enum LengthUnit {

    FEET(1.0),
    INCHES(1.0/12.0),
    YARDS(3.0),
    CENTIMETERS(0.0328084);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor){
        this.toFeetFactor=toFeetFactor;

    }

    public double toFeet(double value){
        return value * toFeetFactor;
    }

    public double fromFeet(double feetValue){
        return feetValue/toFeetFactor;
    }
}
