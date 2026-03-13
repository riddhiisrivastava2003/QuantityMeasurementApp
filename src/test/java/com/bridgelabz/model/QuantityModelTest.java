package com.bridgelabz.model;

import com.bridgelabz.generic_quantity.LengthUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityModelTest {

    @Test
    void givenValueAndUnit_WhenCreated_ShouldStoreValues() {

        QuantityModel<LengthUnit> model =
                new QuantityModel<>(10, LengthUnit.FEET);

        assertEquals(10, model.getValue());
        assertEquals(LengthUnit.FEET, model.getUnit());
    }
}
