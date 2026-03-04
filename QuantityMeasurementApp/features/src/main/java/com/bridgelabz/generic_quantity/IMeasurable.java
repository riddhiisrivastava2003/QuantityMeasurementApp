package com.bridgelabz.generic_quantity;



public interface IMeasurable {


    double getConversionFactor();


    default double convertToBaseUnit(double value) {

        return value * getConversionFactor();
    }

    default double convertFromBaseUnit(double baseValue) {


        return baseValue / getConversionFactor();
    }

    String getUnitName();

    default boolean supportsArithmetic() {return true;}
}