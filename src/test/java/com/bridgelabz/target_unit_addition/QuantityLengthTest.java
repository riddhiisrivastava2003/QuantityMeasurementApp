package com.bridgelabz.target_unit_addition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class QuantityLengthTest {



    private static final double EPSILON = 0.01;



    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        QuantityLength result =
                new QuantityLength(1, LengthUnit.FEET)
                        .add(new QuantityLength(12, LengthUnit.INCHES),
                                LengthUnit.FEET);


        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }


    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        QuantityLength result =
                new QuantityLength(1, LengthUnit.FEET)
                        .add(new QuantityLength(12, LengthUnit.INCHES),
                                LengthUnit.INCHES);

        assertEquals(24.0, result.getValue(), EPSILON);
    }


    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        QuantityLength result =
                new QuantityLength(1, LengthUnit.FEET)
                        .add(new QuantityLength(12, LengthUnit.INCHES),
                                LengthUnit.YARDS);

        assertEquals(0.67, result.getValue(), EPSILON);
    }


    @Test
    void testAddition_Commutativity() {
        QuantityLength a =
                new QuantityLength(1, LengthUnit.FEET);

        QuantityLength b =
                new QuantityLength(12, LengthUnit.INCHES);

        QuantityLength r1 = a.add(b, LengthUnit.YARDS);
        QuantityLength r2 = b.add(a, LengthUnit.YARDS);

        assertEquals(r1.getValue(), r2.getValue(), EPSILON);
    }



    @Test
    void testAddition_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1, LengthUnit.FEET)
                    .add(new QuantityLength(12, LengthUnit.INCHES),
                            null);
        });
    }


    @Test
    void testAddition_NegativeValues() {
        QuantityLength result =
                new QuantityLength(5, LengthUnit.FEET)
                        .add(new QuantityLength(-2, LengthUnit.FEET),
                                LengthUnit.INCHES);

        assertEquals(36.0, result.getValue(), EPSILON);
    }
}