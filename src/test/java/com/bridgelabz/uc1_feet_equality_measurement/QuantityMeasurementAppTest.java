package com.bridgelabz.uc1_feet_equality_measurement;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class QuantityMeasurementAppTest {

    @Test
    void testEquality_SameValue(){

        QuantityMeasurementApp.Feet f1=new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2=new QuantityMeasurementApp.Feet(1.0);
        assertTrue(f1.equals(f2), "1.0 ft should be equal to 1.0 ft");

    }

    @Test
    void testEquality_DifferentValue(){
        QuantityMeasurementApp.Feet f1=new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2=new QuantityMeasurementApp.Feet(2.0);
        assertFalse(f1.equals(f2), "1.0 ft should not be equal to 2.0 ft");

    }

    @Test
    void testEquality_NullComparison(){
        QuantityMeasurementApp.Feet f1=new QuantityMeasurementApp.Feet(1.0);
        assertFalse(f1.equals(null), "Comparison with null should return false");
    }

    @Test
    void testEquality_NonNumberInput(){
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.validateInput("abc"));
    }

    @Test
    void testEqaulity_SameReference(){
        QuantityMeasurementApp.Feet f1=new QuantityMeasurementApp.Feet(1.0);
        assertTrue(f1.equals(f1), "same refrence must return true");
    }
}
