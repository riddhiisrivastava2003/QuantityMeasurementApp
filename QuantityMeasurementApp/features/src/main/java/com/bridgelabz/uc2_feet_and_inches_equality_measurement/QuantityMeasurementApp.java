package com.bridgelabz.uc2_feet_and_inches_equality_measurement;

import java.util.*;

public class QuantityMeasurementApp {

    public static double validateInput(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input");
        }
    }


    public static boolean checkFeetEquality(double value1, double value2) {
        Feet feet1 = new Feet(value1);
        Feet feet2 = new Feet(value2);
        boolean result = feet1.equals(feet2);


        System.out.println("Input: " + value1 + " ft and " + value2 + " ft");
        System.out.println("Result: " + result);
        return result;


    }

    public static boolean checkInchesEquality(double value1, double value2) {
        Inches inches1 = new Inches(value1);
        Inches inches2 = new Inches(value2);
        boolean result = inches1.equals(inches2);


        System.out.println("Input: " + value1 + " inch and " + value2 + " inch");
        System.out.println("Result: " + result);
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Enter first feet value:");
            double feet1 = validateInput(sc.nextLine());
            System.out.println("Enter second feet value: ");
            double feet2 = validateInput(sc.nextLine());
            checkFeetEquality(feet1, feet2);


            System.out.println("Enter first inch value:");
            double inch1 = validateInput(sc.nextLine());
            System.out.println("Enter second inch value: ");
            double inch2 = validateInput(sc.nextLine());
            checkInchesEquality(inch1, inch2);
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }


}
