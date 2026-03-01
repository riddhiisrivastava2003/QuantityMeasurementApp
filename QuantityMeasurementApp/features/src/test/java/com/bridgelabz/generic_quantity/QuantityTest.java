package com.bridgelabz.generic_quantity;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {


    @Test
    void testIMeasurable_LengthUnitImplementation() {


        assertTrue(LengthUnit.FEET instanceof IMeasurable);

        assertEquals(1.0, LengthUnit.FEET.getConversionFactor());
    }

    @Test
    void testIMeasurable_WeightUnitImplementation() {

        assertTrue(WeightUnit.KILOGRAM instanceof IMeasurable);

        assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor());
    }


    @Test
    void testGenericQuantity_LengthEquality() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

        assertEquals(q1, q2);
    }


    @Test
    void testGenericQuantity_WeightEquality() {

        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        assertEquals(q1, q2);
    }


    @Test
    void testGenericQuantity_LengthConversion() {

        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = q.convertTo(LengthUnit.INCHES);

        assertEquals(new Quantity<>(12.0, LengthUnit.INCHES), result);
    }

    @Test
    void testGenericQuantity_WeightConversion() {
        Quantity<WeightUnit> q = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> result = q.convertTo(WeightUnit.GRAM);

        assertEquals(new Quantity<>(1000.0, WeightUnit.GRAM), result);
    }

    @Test
    void testGenericQuantity_LengthAddition() {

        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = q1.add(q2, LengthUnit.FEET);

        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
    }

    @Test
    void testGenericQuantity_WeightAddition() {


        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);


        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result = q1.add(q2, WeightUnit.KILOGRAM);

        assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM), result);
    }



    @Test
    void testCrossCategoryPrevention_Equals() {

        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertNotEquals(length, weight);
    }


    @Test
    void testConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class, () ->
                new Quantity<>(1.0, null));
    }

    @Test
    void testConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testHashCode_Consistency() {


        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

        assertEquals(q1.hashCode(), q2.hashCode());
    }


    @Test
    void testImmutability() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = q1.convertTo(LengthUnit.INCHES);

        assertNotSame(q1, q2);
    }

}