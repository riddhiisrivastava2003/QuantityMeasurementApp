package com.bridgelabz.uc3_generic_quantity;

public enum LengthUnit {

    FEET(1.0),
    INCH(1.0 / 12.0);




    private final double conversionFactorToFeet;


    LengthUnit(double conversionFactorToFeet) {
        this.conversionFactorToFeet = conversionFactorToFeet;
    }



    public double toFeet(double value) {
        return value * conversionFactorToFeet;
    }

}