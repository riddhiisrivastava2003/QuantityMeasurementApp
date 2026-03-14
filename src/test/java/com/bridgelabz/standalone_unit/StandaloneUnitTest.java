package com.bridgelabz.standalone_unit;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class StandaloneUnitTest {

    private static final double EPSILON = 0.0001;

    @Test
    void testConversionFactor_Feet() {


        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), EPSILON);
    }

    @Test
    void testConversionFactor_Inches() {

        assertEquals(1.0 / 12.0, LengthUnit.INCHES.getConversionFactor(), EPSILON);
    }

    @Test
    void testConversionFactor_Yards() {


        assertEquals(3.0, LengthUnit.YARDS.getConversionFactor(), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_Length() {

        assertEquals(1.0, LengthUnit.INCHES.convertToBaseUnit(12.0), EPSILON);


        assertEquals(3.0, LengthUnit.YARDS.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_Length() {

        assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(1.0), EPSILON);

        assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(3.0), EPSILON);
    }

    @Test
    void testEquality_FeetAndInches() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testAdd_Length_ToFeet() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = q1.add(q2, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAdd_Length_ToYards() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = q1.add(q2, LengthUnit.YARDS);

        assertEquals(2.0 / 3.0, result.getValue(), EPSILON);
    }

    @Test
    void testRoundTripConversion_Length() {

        QuantityLength original = new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength inches = original.convertTo(LengthUnit.INCHES);

        QuantityLength backToFeet = inches.convertTo(LengthUnit.FEET);
        assertEquals(original.getValue(), backToFeet.getValue(), EPSILON);
    }


    @Test
    void testConversionFactor_Kilogram() {
        assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor(), EPSILON);
    }

    @Test

    void testConversionFactor_Gram() {

        assertEquals(0.001, WeightUnit.GRAM.getConversionFactor(), EPSILON);
    }

    @Test
    void testConversionFactor_Pound() {
        assertEquals(2.20462, WeightUnit.POUND.convertFromBaseUnit(1.0), 0.01);
    }

    @Test
    void testEquality_KgAndGram() {


        QuantityWeight q1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight q2 = new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertTrue(q1.equals(q2));


    }

    @Test
    void testConvert_KgToPound() {

        QuantityWeight q = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight result = q.convertTo(WeightUnit.POUND);
        assertEquals(2.20462, result.getValue(), 0.01);
    }

    @Test
    void testAdd_Weight_ToKg() {

        QuantityWeight q1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);


        QuantityWeight q2 = new QuantityWeight(1000.0, WeightUnit.GRAM);

        QuantityWeight result = q1.add(q2, WeightUnit.KILOGRAM);
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testConvert_GramToKg() {
        QuantityWeight q = new QuantityWeight(2000.0, WeightUnit.GRAM);
        QuantityWeight result = q.convertTo(WeightUnit.KILOGRAM);
        assertEquals(2.0, result.getValue(), EPSILON);
    }


    @Test
    void testNullUnit_Length() {

        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testInvalidValue_Length() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testZeroAddition_Weight() {

        QuantityWeight q1 = new QuantityWeight(5.0, WeightUnit.KILOGRAM);

        QuantityWeight q2 = new QuantityWeight(0.0, WeightUnit.GRAM);

        QuantityWeight result = q1.add(q2, WeightUnit.KILOGRAM);
        assertEquals(5.0, result.getValue(), EPSILON);
        
    }
}