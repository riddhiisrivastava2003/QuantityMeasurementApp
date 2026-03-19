package com.bridgelabz.unit.generic_quantity;

public class UnitFactory {

    public static IMeasurable getUnit(String unit) {

        switch (unit.toUpperCase()) {

            // LENGTH
            case "INCHES":
            case "FEET":
            case "YARD":
                return LengthUnit.valueOf(unit.toUpperCase());

            // WEIGHT
            case "KG":
            case "GRAM":
                return WeightUnit.valueOf(unit.toUpperCase());

            // TEMPERATURE
            case "CELSIUS":
            case "FAHRENHEIT":
                return TemperatureUnit.valueOf(unit.toUpperCase());

            default:
                throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }
}