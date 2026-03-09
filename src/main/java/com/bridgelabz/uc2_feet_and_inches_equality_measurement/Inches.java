package com.bridgelabz.uc2_feet_and_inches_equality_measurement;

public class Inches {

    private final double value;

    public Inches(double value){
        if(value<0){
            throw new IllegalArgumentException("Inches value cannot be negative");
        }
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
        Inches other=(Inches)obj;
        return Double.compare(this.value, other.value)==0;
    }

    @Override
    public int hashCode(){
        return Double.hashCode(value);
    }
}
