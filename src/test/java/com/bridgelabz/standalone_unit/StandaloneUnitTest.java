package com.bridgelabz.standalone_unit;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StandaloneUnitTest
{




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


    void testConversionFactor_Centimeters() {
        assertEquals(1.0 / 30.48, LengthUnit.CENTIMETERS.getConversionFactor(), EPSILON);
    }

    @Test
    void testConvertToBaseUnit() {
        assertEquals(1.0, LengthUnit.INCHES.convertToBaseUnit(12.0), EPSILON);
        assertEquals(3.0, LengthUnit.YARDS.convertToBaseUnit(1.0), EPSILON);
        assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(30.48), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit() {

        assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(1.0), EPSILON);

        assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(3.0), EPSILON);

        assertEquals(30.48, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), EPSILON);
    }


    @Test
    void testEquality_FeetAndInches() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testConvertTo() {


        QuantityLength q = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength result = q.convertTo(LengthUnit.INCHES);

        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testAdd_ToFeet() {


        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);


        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = q1.add(q2, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test

    void testAdd_WithTargetUnit_Yards() {



        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = q1.add(q2, LengthUnit.YARDS);

        assertEquals(2.0 / 3.0, result.getValue(), EPSILON);
    }

    @Test
    void testRoundTripConversion() {

        QuantityLength original = new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength inches = original.convertTo(LengthUnit.INCHES);


        QuantityLength backToFeet = inches.convertTo(LengthUnit.FEET);

        assertEquals(original.getValue(), backToFeet.getValue(), EPSILON);
    }

    @Test
    void testNullUnit() {

        assertThrows(IllegalArgumentException.class,

                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testInvalidValue() {

        assertThrows(IllegalArgumentException.class,


                () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testZeroAddition() {

        QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);


        QuantityLength q2 = new QuantityLength(0.0, LengthUnit.INCHES);

        QuantityLength result = q1.add(q2, LengthUnit.FEET);

        assertEquals(5.0, result.getValue(), EPSILON);
    }
}