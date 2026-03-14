package com.bridgelabz.target_unit_addition;

import java.util.Scanner;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {



            System.out.print("Enter first value: ");
            double value1 = sc.nextDouble();



            System.out.print("Enter first unit (FEET, INCHES, YARDS, CENTIMETERS): ");
            LengthUnit unit1 = LengthUnit.valueOf(sc.next().toUpperCase());

            QuantityLength q1 = new QuantityLength(value1, unit1);



            System.out.print("Enter second value: ");
            double value2 = sc.nextDouble();



            System.out.print("Enter second unit (FEET, INCHES, YARDS, CENTIMETERS): ");
            LengthUnit unit2 = LengthUnit.valueOf(sc.next().toUpperCase());



            QuantityLength q2 = new QuantityLength(value2, unit2);



            System.out.print("Enter target unit (FEET, INCHES, YARDS, CENTIMETERS): ");
            LengthUnit targetUnit = LengthUnit.valueOf(sc.next().toUpperCase());



            QuantityLength result = q1.add(q2, targetUnit);

            System.out.println("\nResult: " + result);


        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input! Please enter valid units and numeric values.");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred.");
        } finally {
            sc.close();
        }
    }
}