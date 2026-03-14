package com.bridgelabz.generic_quantity;


//public enum LengthUnit implements IMeasurable {
//
//    FEET(1.0),
//    INCHES(1.0 / 12.0),
//
//    YARDS(3.0),
//
//    CENTIMETERS(1.0 / 30.48);
//
//    private final double conversionFactor;
//
//
//
//
//
//    LengthUnit(double conversionFactor) {
//
//        this.conversionFactor = conversionFactor;
//    }
//
//    @Override
//    public double getConversionFactor() {
//
//
//        return conversionFactor;
//    }
//
//    @Override
//    public String getUnitName() {
//
//        return name();
//    }
//}
public enum LengthUnit implements IMeasurable {

    INCHES(1),
    FEET(12),
    YARD(36);

    private final double conversionFactor;

    LengthUnit(double factor){
        this.conversionFactor = factor;
    }

    @Override
    public double getConversionFactor(){
        return conversionFactor;
    }

    @Override
    public String getUnitName(){
        return this.name();
    }

    @Override
    public String getMeasurementType() {
        return "LENGTH";
    }
}