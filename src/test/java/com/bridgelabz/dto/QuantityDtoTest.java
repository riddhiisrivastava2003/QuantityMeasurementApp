package com.bridgelabz.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityDTOTest {

    @Test
    void givenDTO_WhenCreated_ShouldReturnValues() {

        QuantityDTO dto = new QuantityDTO(10,"FEET");

        assertEquals(10,dto.getValue());
        assertEquals("FEET",dto.getUnit());
    }
}
