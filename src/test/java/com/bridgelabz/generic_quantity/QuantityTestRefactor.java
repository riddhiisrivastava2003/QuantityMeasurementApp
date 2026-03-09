package com.bridgelabz.generic_quantity;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTestRefactor {


    @Test


    void givenFeet_whenConvertedToInch_shouldReturnCorrectValue()

    {
        Quantity<LengthUnit> length = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> result = length.convertTo(LengthUnit.INCHES);
        assertEquals(new Quantity<>(12, LengthUnit.INCHES), result);
    }



    @Test


    void givenTwoSameUnitLengths_whenAdded_shouldReturnSum() {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(1, LengthUnit.FEET);
        assertEquals(new Quantity<>(2, LengthUnit.FEET), q1.add(q2));
    }



    @Test


    void givenDifferentLengthUnits_whenAdded_shouldReturnCorrectResult() {

        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6, LengthUnit.INCHES);
        assertEquals(new Quantity<>(18, LengthUnit.INCHES), q1.add(q2, LengthUnit.INCHES));
    }

    @Test


    void givenDifferentCategories_whenAdded_shouldThrowException() {


        Quantity<LengthUnit> length = new Quantity<>(1, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> length.add((Quantity) weight));
    }



    @Test


    void givenTwoLengths_whenSubtracted_shouldReturnCorrectValue() {
        Quantity<LengthUnit> q1 = new Quantity<>(2, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6, LengthUnit.INCHES);
        assertEquals(new Quantity<>(18, LengthUnit.INCHES), q1.subtract(q2, LengthUnit.INCHES));
    }



    @Test


    void givenNullQuantity_whenSubtracted_shouldThrowException() {Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(null));
    }



    @Test


    void givenTwoSameLengths_whenDivided_shouldReturnRatio()

    {
        Quantity<LengthUnit> q1 = new Quantity<>(2, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(1, LengthUnit.FEET);
        assertEquals(2.0, q1.divide(q2));
    }



    @Test


    void givenDivideByZero_whenDivided_shouldThrowException() {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0, LengthUnit.FEET);
        assertThrows(ArithmeticException.class, () -> q1.divide(q2));
    }



    @Test


    void givenEqualQuantitiesInDifferentUnits_shouldReturnTrue()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);
        assertEquals(q1, q2);
    }


    @Test


    void givenDifferentQuantities_shouldReturnFalse() {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);
        assertNotEquals(q1, q2);
    }



    @Test


    void givenNullUnit_whenCreatingQuantity_shouldThrowException()
    {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10, null));
    }

    @Test


    void givenInfiniteValue_whenCreatingQuantity_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    
}