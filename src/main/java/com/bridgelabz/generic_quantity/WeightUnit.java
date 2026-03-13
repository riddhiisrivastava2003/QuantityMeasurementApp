package com.bridgelabz.generic_quantity;


//public enum WeightUnit implements IMeasurable {
//
//
//    KILOGRAM(1.0),
//
//    GRAM(0.001),
//
//    TONNE(1000.0);
//
//    private final double conversionFactor;
//
//
//
//    WeightUnit(double conversionFactor) {
//
//
//        this.conversionFactor = conversionFactor;
//    }
//
//    @Override
//    public double getConversionFactor() {
//
//        return conversionFactor;
//    }
//
//    @Override
//    public String getUnitName() {
//
//        return name();
//    }

public enum WeightUnit implements IMeasurable {


    KILOGRAM(1000),

    GRAM(1),

    TONNE(1000.0);

    private final double conversionFactor;
    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }
    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public String getUnitName() {

        return name();
    }
    @Override
    public String getMeasurementType() {
        return "WEIGHT";
    }
}