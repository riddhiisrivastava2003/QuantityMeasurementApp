package com.bridgelabz.generic_quantity;

//public enum VolumeUnit implements IMeasurable {
//
//    LITRE(1.0),
//
//    MILLILITRE(0.001),
//
//    GALLON(3.78541);
//
//    private final double conversionFactor;
//
//    VolumeUnit(double conversionFactor)
//    {
//        this.conversionFactor = conversionFactor;
//    }
//
//    @Override
//    public double getConversionFactor()
//    {
//
//        return conversionFactor;
//    }
//
//    @Override
//    public String getUnitName()
//
//    {
//        return name();
//    }
//}
public enum VolumeUnit implements IMeasurable {

    LITRE(1000),
    MILLILITRE(1),
    GALLON(3.78541);
    private final double conversionFactor ;

    VolumeUnit(double conversionFactor)
    {
        this.conversionFactor = conversionFactor;
    }
    @Override
    public double getConversionFactor()
    {

        return conversionFactor;
    }
    @Override
    public String getMeasurementType() {
        return "VOLUME";
    }
    @Override
    public String getUnitName()

    {
        return name();
    }
}