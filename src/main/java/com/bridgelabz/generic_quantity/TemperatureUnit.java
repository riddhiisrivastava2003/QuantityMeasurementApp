package com.bridgelabz.generic_quantity;

//public enum TemperatureUnit implements IMeasurable {
//
//    CELSIUS, FAHRENHEIT;
//
//    @Override
//
//    public double getConversionFactor() {return 1; }
//
//    @Override
//
//    public double convertToBaseUnit(double value) {
//
//        if (this == CELSIUS) return value;
//
//        if (this == FAHRENHEIT) return (value - 32) * 5 / 9;
//
//        return value;
//    }
//
//    @Override
//
//    public double convertFromBaseUnit(double baseValue) {
//
//        if (this == CELSIUS) return baseValue;
//
//        if (this == FAHRENHEIT) return (baseValue * 9 / 5) + 32;
//
//        return baseValue;
//    }
//
//
//
//    @Override
//
//    public String getUnitName() {return name();}
//
//
//    @Override
//
//    public boolean supportsArithmetic() {return false;}


public enum TemperatureUnit implements IMeasurable {

    CELSIUS, FAHRENHEIT;
    @Override
    public double getConversionFactor() {return 1; }

    @Override

    public double convertToBaseUnit(double value) {

        if (this == CELSIUS) return value;

        if (this == FAHRENHEIT) return (value - 32) * 5 / 9;

        return value;
    }
    @Override

    public double convertFromBaseUnit(double baseValue) {

        if (this == CELSIUS) return baseValue;
        if (this == FAHRENHEIT) return (baseValue * 9 / 5) + 32;

        return baseValue;
    }
    @Override

    public String getUnitName() {return name();}

    @Override
    public String getMeasurementType() {
        return "TEMPERATURE";
    }

    @Override

    public boolean supportsArithmetic() {return false;}
}