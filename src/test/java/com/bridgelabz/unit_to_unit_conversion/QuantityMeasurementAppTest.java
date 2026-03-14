package com.bridgelabz.unit_to_unit_conversion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLength.convert(1.0,
                LengthUnit.FEET,
                LengthUnit.INCHES);

        assertEquals(12.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLength.convert(24.0,
                LengthUnit.INCHES,
                LengthUnit.FEET);

        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLength.convert(1.0,
                LengthUnit.YARDS,
                LengthUnit.INCHES);

        assertEquals(36.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        double result = QuantityLength.convert(72.0,
                LengthUnit.INCHES,
                LengthUnit.YARDS);

        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLength.convert(2.54,
                LengthUnit.CENTIMETERS,
                LengthUnit.INCHES);

        assertEquals(1.0, result, 1e-3);
    }


    @Test
    void testConversion_FeetToYards() {
        double result = QuantityLength.convert(6.0,
                LengthUnit.FEET,
                LengthUnit.YARDS);

        assertEquals(2.0, result, EPSILON);
    }



    @Test
    void testConversion_RoundTrip_PreservesValue() {

        double original = 5.0;

        double converted =
                QuantityLength.convert(original,
                        LengthUnit.FEET,
                        LengthUnit.INCHES);

        double roundTrip =
                QuantityLength.convert(converted,
                        LengthUnit.INCHES,
                        LengthUnit.FEET);

        assertEquals(original, roundTrip, EPSILON);
    }


    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLength.convert(0.0,
                LengthUnit.FEET,
                LengthUnit.INCHES);

        assertEquals(0.0, result, EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLength.convert(-1.0,
                LengthUnit.FEET,
                LengthUnit.INCHES);

        assertEquals(-12.0, result, EPSILON);
    }


    @Test
    void testConversion_SameUnit() {
        double result = QuantityLength.convert(5.0,
                LengthUnit.FEET,
                LengthUnit.FEET);

        assertEquals(5.0, result, EPSILON);
    }



    @Test
    void testConversion_NullSourceUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(1.0,
                        null,
                        LengthUnit.FEET));
    }

    @Test
    void testConversion_NullTargetUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(1.0,
                        LengthUnit.FEET,
                        null));
    }



    @Test
    void testConversion_NaN_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(Double.NaN,
                        LengthUnit.FEET,
                        LengthUnit.INCHES));
    }

    @Test
    void testConversion_Infinite_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(Double.POSITIVE_INFINITY,
                        LengthUnit.FEET,
                        LengthUnit.INCHES));
    }

  

    @Test
    void testEquality_DifferentUnits_SameLength() {

        QuantityLength l1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength l2 =
                new QuantityLength(12.0, LengthUnit.INCHES);

        assertTrue(l1.equals(l2));
    }

    @Test
    void testEquality_DifferentLength() {

        QuantityLength l1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength l2 =
                new QuantityLength(2.0, LengthUnit.FEET);

        assertFalse(l1.equals(l2));
    }
}