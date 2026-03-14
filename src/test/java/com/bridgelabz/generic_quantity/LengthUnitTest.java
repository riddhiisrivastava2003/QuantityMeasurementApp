package com.bridgelabz.generic_quantity;

import com.bridgelabz.generic_quantity.LengthUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class LengthUnitTest {

    @Test
    void givenFeet_WhenConvertedToBaseUnit_ShouldReturnCorrectValue() {
        double result = LengthUnit.FEET.convertToBaseUnit(1);
        assertEquals(12, result, 0.001);
    }

    @Test
    void givenInch_WhenConvertedToBaseUnit_ShouldReturnCorrectValue() {
        double result = LengthUnit.INCHES.convertToBaseUnit(12);
        assertEquals(12, result, 0.001);
    }
}