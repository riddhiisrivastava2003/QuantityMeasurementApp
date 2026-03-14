package com.bridgelabz.generic_quantity;

//import java.util.Objects;
//
//public final class Quantity<U extends IMeasurable> {
//
//    private final double value;
//
//    private final U unit;
//
//    public Quantity(double value, U unit) {
//
//
//        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
//
//        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
//
//        this.value = value;
//
//        this.unit = unit;
//    }
//
//    public double getValue() {
//        return value;
//    }
//
//    public U getUnit() {
//        return unit;
//    }
//
//    public Quantity<U> convertTo(U targetUnit) {
//
//        if (!unit.getClass().equals(targetUnit.getClass())) throw new IllegalArgumentException("Cannot convert between different measurement categories");
//
//        double baseValue = unit.convertToBaseUnit(value);
//
//        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
//
//        return new Quantity<>(round(convertedValue), targetUnit);
//    }
//
//    public Quantity<U> add(Quantity<U> other) {
//        return add(other, this.unit);
//    }
//
//    public Quantity<U> add(Quantity<U> other, U targetUnit) {
//
//        if (!unit.getClass().equals(other.unit.getClass())) throw new IllegalArgumentException("Cannot add different measurement categories");
//
//        double base1 = unit.convertToBaseUnit(value);
//
//
//        double base2 = other.unit.convertToBaseUnit(other.value);
//
//        double sumBase = base1 + base2;
//
//
//        double finalValue = targetUnit.convertFromBaseUnit(sumBase);
//
//        return new Quantity<>(round(finalValue), targetUnit);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//
//        if (this == obj) return true;
//        if (!(obj instanceof Quantity<?> that)) return false;
//
//        if (!unit.getClass().equals(that.unit.getClass())) return false;
//
//        double base1 = unit.convertToBaseUnit(value);
//
//
//        double base2 = that.unit.convertToBaseUnit(that.value);
//
//        return Double.compare(round(base1), round(base2)) == 0;
//    }
//
//    @Override
//    public int hashCode() {
//
//        double base = round(unit.convertToBaseUnit(value));
//
//        return Objects.hash(base, unit.getClass());
//    }
//
//    @Override
//    public String toString() {
//
//        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
//    }
//
//    private double round(double value) {
//
//        return Math.round(value * 100.0) / 100.0;
//    }
//
//    public Quantity<U> subtract(Quantity<U> other) {
//        return subtract(other, this.unit);
//    }
//
//    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
//
//        if (other == null)
//            throw new IllegalArgumentException("Quantity cannot be null");
//
//        if (targetUnit == null)
//            throw new IllegalArgumentException("Target unit cannot be null");
//
//        if (!this.unit.getClass().equals(other.unit.getClass()))
//            throw new IllegalArgumentException("Cross-category operation not allowed");
//
//        double base1 = this.unit.convertToBaseUnit(this.value);
//        double base2 = other.unit.convertToBaseUnit(other.value);
//
//        double baseResult = base1 - base2;
//
//        double finalValue = targetUnit.convertFromBaseUnit(baseResult);
//
//        finalValue = Math.round(finalValue * 100.0) / 100.0;
//
//        return new Quantity<>(finalValue, targetUnit);
//    }
//
//    public double divide(Quantity<U> other) {
//
//        if (other == null)
//            throw new IllegalArgumentException("Quantity cannot be null");
//
//        if (!this.unit.getClass().equals(other.unit.getClass()))
//            throw new IllegalArgumentException("Cross-category operation not allowed");
//
//        double base1 = this.unit.convertToBaseUnit(this.value);
//        double base2 = other.unit.convertToBaseUnit(other.value);
//
//        if (base2 == 0)
//            throw new ArithmeticException("Division by zero");
//
//        return base1 / base2;
//    }
//}




import java.util.Objects;
import java.util.function.DoubleBinaryOperator;



public final class  Quantity<U extends IMeasurable>
{

    private final double value;

    private final U unit;

    public Quantity(double value, U unit)
    {

        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");



        this.value = value;
        this.unit = unit;
    }

    public double getValue() {return value;}

    public U getUnit() {return unit;}



    private enum ArithmeticOperation
    {

        ADD((a, b) -> a + b), SUBTRACT((a, b) -> a - b), DIVIDE((a, b) ->
    {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        });



        private final DoubleBinaryOperator operator;



        ArithmeticOperation(DoubleBinaryOperator operator) {this.operator = operator;}

        public double compute(double a, double b) {return operator.applyAsDouble(a, b);}
    }



    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetRequired) {



        if (other == null) throw new IllegalArgumentException("Quantity cannot be null");

        if (!this.unit.getClass().equals(other.unit.getClass())) throw new IllegalArgumentException("Cross-category operation not allowed");

        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) throw new IllegalArgumentException("Values must be finite");

        if (!this.unit.supportsArithmetic()) throw new UnsupportedOperationException("Arithmetic not supported for " + unit.getUnitName());

        if (targetRequired && targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");

    }



    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation)
    {



        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        return operation.compute(base1, base2);
    }



    public Quantity<U> add(Quantity<U> other) {return add(other, this.unit);}

    public Quantity<U> add(Quantity<U> other, U targetUnit)
    {

        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double finalValue = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(round(finalValue), targetUnit);
    }


    public Quantity<U> subtract(Quantity<U> other) {return subtract(other, this.unit);}


    public Quantity<U> subtract(Quantity<U> other, U targetUnit)

    {

        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double finalValue = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(round(finalValue), targetUnit);
    }





    public double divide(Quantity<U> other) {


        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }


    public Quantity<U> convertTo(U targetUnit)

    {

        if (!unit.getClass().equals(targetUnit.getClass())) throw new IllegalArgumentException("Cannot convert between different measurement categories");



        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(round(convertedValue), targetUnit);
    }



    @Override


    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof Quantity<?> that)) return false;

        if (!unit.getClass().equals(that.unit.getClass())) return false;
        double base1 = unit.convertToBaseUnit(value);


        double base2 = that.unit.convertToBaseUnit(that.value);



        return Double.compare(round(base1), round(base2)) == 0;
    }

    @Override


    public int hashCode()
    {
        double base = round(unit.convertToBaseUnit(value));
        return Objects.hash(base, unit.getClass());
    }

    @Override


    public String toString() {return "Quantity(" + value + ", " + unit.getUnitName() + ")";}



    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}