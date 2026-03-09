package com.bridgelabz.generic_quantity;

public class QuantityMeasurementApp {


    public static <U extends IMeasurable> void demonstrateEquality(

            Quantity<U> q1, Quantity<U> q2) {


        System.out.println(q1 + " equals " + q2 + " ? " + q1.equals(q2));
    }



    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> quantity, U targetUnit) {



        System.out.println(quantity + " converted to " + targetUnit.getUnitName() + " = " + quantity.convertTo(targetUnit));
    }

    public static <U extends IMeasurable> void demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit)
    {

        System.out.println(q1 + " + " + q2 + " = " + q1.add(q2, targetUnit));
    }

    public static void main(String[] args) {

        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

        demonstrateEquality(l1, l2);

        demonstrateConversion(l1, LengthUnit.INCHES);

        demonstrateAddition(l1, l2, LengthUnit.FEET);

        System.out.println();


        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        demonstrateEquality(w1, w2);

        demonstrateConversion(w1, WeightUnit.GRAM);

        demonstrateAddition(w1, w2, WeightUnit.KILOGRAM);
    }
}