package com.bridgelabz.unit_addition;


public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = q1.add(q2);

        System.out.println(result);

        System.out.println(
                new QuantityLength(12, LengthUnit.INCHES)
                        .add(new QuantityLength(1, LengthUnit.FEET))
        );

        System.out.println(
                new QuantityLength(1, LengthUnit.YARDS)
                        .add(new QuantityLength(3, LengthUnit.FEET))
        );

        System.out.println(
                new QuantityLength(2.54, LengthUnit.CENTIMETERS)
                        .add(new QuantityLength(1, LengthUnit.INCHES))
        );
    }
}