package com.bridgelabz.generic_quantity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityArithmeticTest {
    private static final double EPSILON = 0.0001;


    @Test

    void testSubtraction_SameUnit()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(5.0, result.getValue(), EPSILON);

        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test

    void testSubtraction_CrossUnit()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(9.5, result.getValue(), EPSILON);
    }



    @Test

    void testSubtraction_ResultZero()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(120.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = q1.subtract(q2);

        assertEquals(0.0, result.getValue(), EPSILON);
    }

    @Test

    void testSubtraction_ResultNegative()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);

        assertEquals(-5.0, result.getValue(), EPSILON);
    }

    @Test

    void testSubtraction_NullOperand()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(null));
    }

    @Test

    void testSubtraction_CrossCategory()
    {
        Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> length.subtract((Quantity) weight));
    }

    @Test


    void testSubtraction_Immutability()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        q1.subtract(q2);
        assertEquals(10.0, q1.getValue(), EPSILON);
    }



    @Test

    void testDivision_SameUnit()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        double result = q1.divide(q2);
        assertEquals(5.0, result, EPSILON);
    }

    @Test

    void testDivision_CrossUnit()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(24.0, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        double result = q1.divide(q2);
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testDivision_RatioLessThanOne()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(10.0, LengthUnit.FEET);
        assertEquals(0.5, q1.divide(q2), EPSILON);
    }

    @Test


    void testDivision_RatioEqualToOne()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(10.0, LengthUnit.FEET);
        assertEquals(1.0, q1.divide(q2), EPSILON);
    }

    @Test

    void testDivision_NonCommutative()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        assertEquals(2.0, q1.divide(q2), EPSILON);
        assertEquals(0.5, q2.divide(q1), EPSILON);
    }

    @Test


    void testDivision_ByZero()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.FEET);
        assertThrows(ArithmeticException.class, () -> q1.divide(q2));
    }

    @Test


    void testDivision_NullOperand()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.divide(null));
    }

    @Test

    void testDivision_CrossCategory()
    {
        Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> length.divide((Quantity) weight));
    }

    @Test

    void testDivision_Immutability()
    {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        q1.divide(q2);
        assertEquals(10.0, q1.getValue(), EPSILON);
    }
}