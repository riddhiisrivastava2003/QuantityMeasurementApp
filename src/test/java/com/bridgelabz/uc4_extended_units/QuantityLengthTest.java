package com.bridgelabz.uc4_extended_units;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {



    @Test
    void testEquality_YardToYard_SameValue() {


        assertTrue(new QuantityLength(1.0, LengthUnit.YARDS)
                .equals(new QuantityLength(1.0, LengthUnit.YARDS)));
    }



    @Test
    void testEquality_YardToFeet_EquivalentValue() {


        assertTrue(new QuantityLength(1.0, LengthUnit.YARDS)
                .equals(new QuantityLength(3.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {

        assertTrue(new QuantityLength(1.0, LengthUnit.YARDS)

                .equals(new QuantityLength(36.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_CentimeterToInch_EquivalentValue() {

        assertTrue(new QuantityLength(1.0, LengthUnit.CENTIMETERS)
                .equals(new QuantityLength(0.393701, LengthUnit.INCH)));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {

        assertFalse(new QuantityLength(1.0, LengthUnit.YARDS)
                .equals(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {


        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);

        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(inches));
        assertTrue(yard.equals(inches));
    }

    @Test
    void testEquality_NullComparison() {

        assertFalse(new QuantityLength(1.0, LengthUnit.YARDS).equals(null));
    }

    @Test
    void testEquality_InvalidUnit() {
        assertThrows(IllegalArgumentException.class, () -> {

            new QuantityLength(1.0, null);
        });
    }
}
