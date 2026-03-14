package com.bridgelabz.uc1_feet_equality_measurement;

import java.util.Objects;
import java.util.*;
public class QuantityMeasurementApp {

    public static class Feet{
        private final double value;

        public Feet(double value){
            this.value=value;
        }

        public double getValue(){
            return value;
        }

        @Override
        public boolean equals(Object obj){
            if(this==obj){
                return true;
            }
            if(obj==null||getClass()!=obj.getClass()){
                return false;
            }
            Feet other=(Feet)obj;
            return Double.compare(this.value, other.value)==0;
        }

        @Override
        public int hashCode(){
            return Objects.hashCode(value);
        }
    }

    public static double validateInput(String input){
        try{
            return Double.parseDouble(input);
        }catch(NumberFormatException e){
            throw new IllegalArgumentException("Invalid input");
        }

    }

    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);

        System.out.println("Enter first feet value:");
        String input1=sc.nextLine();

        System.out.println("Enter second feet value: ");
        String input2=sc.nextLine();

        try {
            double value1 = validateInput(input1);
            double value2 = validateInput(input2);
            Feet feet1 = new Feet(value1);
            Feet feet2 = new Feet(value2);

            boolean result = feet1.equals(feet2);

            System.out.println("Input: " + value1 + " ft and " + value2 + " ft");
            System.out.println("Result: " + result);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }


    }
}
