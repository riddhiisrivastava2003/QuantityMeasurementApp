package com.bridgelabz.uc4_extended_units;

public enum LengthUnit {


    FEET(1.0),
    INCH(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.393701 / 12.0);



    private final double conversionFactorToFeet;



    LengthUnit(double conversionFactorToFeet) {
        this.conversionFactorToFeet = conversionFactorToFeet;
    }



    public double toFeet(double value) {
        return value * conversionFactorToFeet;
    }
}
