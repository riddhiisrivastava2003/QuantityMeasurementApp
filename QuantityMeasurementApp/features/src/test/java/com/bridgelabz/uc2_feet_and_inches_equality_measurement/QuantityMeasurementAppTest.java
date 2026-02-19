package com.bridgelabz.uc2_feet_and_inches_equality_measurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {
    @Test
    public void testFeetEquality_SameValue() {
        assertTrue(QuantityMeasurementApp.checkFeetEquality(1.0, 1.0));
    }

    @Test
    public void testFeetEquality_DifferentValue() {
        assertFalse(QuantityMeasurementApp.checkInchesEquality(1.0, 2.0));
    }

    @Test
    public void testFeetEquality_NullComparison() {
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            QuantityMeasurementApp.validateInput("abc");
        });

        assertEquals("Invalid input",exception.getMessage());

    }

    @Test
    public void testFeetEquality_SameReference() {
        Feet f1=new Feet(1.0);
        assertTrue(f1.equals(f1));
    }

    @Test
    public void testInchesEquality_SameValue() {
        assertTrue(QuantityMeasurementApp.checkFeetEquality(1.0, 1.0));
    }

    @Test
    public void testInchesEquality_DifferentValue() {
        assertFalse(QuantityMeasurementApp.checkInchesEquality(1.0, 2.0));
    }
    @Test
    public void testInchesEquality_NullComparison() {
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            QuantityMeasurementApp.validateInput("abc");
        });

        assertEquals("Invalid input",exception.getMessage());

    }

    @Test
    public void testInchesEquality_SameReference() {
        Inches i1=new Inches(1.0);
        assertTrue(i1.equals(i1));
    }

    @Test
    public void testInchesEquality_NonNumberInput() {
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            QuantityMeasurementApp.validateInput("abc");
        });
        assertEquals("Invalid input",exception.getMessage());
    }

}
