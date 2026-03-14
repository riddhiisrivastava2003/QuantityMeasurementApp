package com.bridgelabz.unit_to_unit_conversion;


import java.util.Scanner;

public class QuantityMeasurementApp {

    public static void demonstrateLengthConversion(double value,
                                                   LengthUnit from,
                                                   LengthUnit to) {

        double result = QuantityLength.convert(value, from, to);

        System.out.println(value + " " + from + " = " + result + " " + to);
    }

    public static void demonstrateLengthConversion(QuantityLength length,
                                                   LengthUnit to) {

        QuantityLength converted = length.convertTo(to);

        System.out.println(length + " = " + converted);
    }

    public static void demonstrateLengthEquality(QuantityLength l1,
                                                 QuantityLength l2) {

        System.out.println("Are Equal? " + l1.equals(l2));
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {

            System.out.println("Enter value:");
            double value = scanner.nextDouble();

            System.out.println("Available Units:");
            for (LengthUnit unit : LengthUnit.values()) {
                System.out.println(unit);
            }

            System.out.println("Enter Source Unit:");
            LengthUnit from = LengthUnit.valueOf(scanner.next().toUpperCase());

            System.out.println("Enter Target Unit:");
            LengthUnit to = LengthUnit.valueOf(scanner.next().toUpperCase());

            demonstrateLengthConversion(value, from, to);


            double roundTrip =
                    QuantityLength.convert(
                            QuantityLength.convert(value, from, to),
                            to,
                            from
                    );

            System.out.println("Round Trip Result: " + roundTrip);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
