package com.bridgelabz.generic_quantity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementTest {

    @Test


    void shouldConsider1FootEqualTo12Inches() {Quantity<LengthUnit> foot = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches = new Quantity<>(12.0, LengthUnit.INCHES);
        assertEquals(foot, inches);

    }

    @Test

    void shouldAddLengthsCorrectly() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = l1.add(l2, LengthUnit.FEET);

        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);

    }

    @Test

    void shouldSubtractLengthsCorrectly() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> l2 = new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = l1.subtract(l2);

        assertEquals(new Quantity<>(0.5, LengthUnit.FEET), result);
    }



    @Test


    void shouldDivideLengthsCorrectly() {
        Quantity<LengthUnit> l1 = new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> l2 = new Quantity<>(2.0, LengthUnit.FEET);

        assertEquals(5.0, l1.divide(l2));
    }




    @Test


    void shouldConsider1KgEqualTo1000Grams() {
        Quantity<WeightUnit> kg = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> gram = new Quantity<>(1000.0, WeightUnit.GRAM);

        assertEquals(kg, gram);

    }

    @Test


    void shouldAddWeightsCorrectly() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result = w1.add(w2, WeightUnit.KILOGRAM);

        assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM), result);
    }



    @Test


    void shouldConsider1LitreEqualTo1000Ml() {
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertEquals(litre, ml);

    }

    @Test

    void shouldAddVolumesCorrectly() {
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> result = v1.add(v2, VolumeUnit.LITRE);

        assertEquals(new Quantity<>(2.0, VolumeUnit.LITRE), result);

    }



    @Test


    void shouldConvertCelsiusToFahrenheit() {
        Quantity<TemperatureUnit> c = new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> result = c.convertTo(TemperatureUnit.FAHRENHEIT);

        assertEquals(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT), result);

    }

    @Test


    void shouldConsider0CEqualTo32F() {
        Quantity<TemperatureUnit> c = new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> f = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        assertEquals(c, f);

    }

    @Test


    void shouldThrowExceptionWhenAddingTemperature() {
        Quantity<TemperatureUnit> t1 = new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> t2 = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        assertThrows(UnsupportedOperationException.class, () -> t1.add(t2));
    }

    @Test


    void shouldThrowExceptionWhenDividingTemperature() {
        Quantity<TemperatureUnit> t1 = new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> t2 = new Quantity<>(2.0, TemperatureUnit.CELSIUS);

        assertThrows(UnsupportedOperationException.class, () -> t1.divide(t2));

    }



    @Test

     
    void shouldNotAllowCrossCategoryAddition() {
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> length.add((Quantity) weight));
    }
}