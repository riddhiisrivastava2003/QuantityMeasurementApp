package com.bridgelabz.uc2_feet_and_inches_equality_measurement;

import com.bridgelabz.uc1_feet_equality_measurement.QuantityMeasurementApp;

import java.util.Objects;

public class Feet{
    private final double value;

    public Feet(double value){
        if(value<0){
            throw new IllegalArgumentException("feet value cannot be negative");
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
        Feet other = (Feet) obj;
        return Double.compare(this.value, other.value)==0;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(value);
    }
}