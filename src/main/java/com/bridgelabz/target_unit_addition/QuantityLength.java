package com.bridgelabz.target_unit_addition;


import java.util.Objects;
public final class QuantityLength {

    private final double value;
    private final LengthUnit unit;
    private static final double EPSILON=0.0001;


    public QuantityLength(double value,LengthUnit unit){
        if(unit==null|| !Double.isFinite(value)){
            throw new IllegalArgumentException("Invalid value or unit");
        }

        this.value=value;
        this.unit=unit;
    }

    public double getValue(){
        return value;
    }

    public LengthUnit getUnit(){return unit;}


    //uc6
    public QuantityLength add(QuantityLength other){
        return add(other,this.unit);
    }

    //uc7

    public QuantityLength add(QuantityLength other,LengthUnit targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Operands or target unit");
        }

        if (!Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }

        double result = addInBaseUnit(this, other, targetUnit);
        return new QuantityLength(result, targetUnit);
    }

        private static double addInBaseUnit(QuantityLength a,QuantityLength b, LengthUnit targetUnit) {


            double aFeet=a.unit.toFeet(a.value);

            double bFeet=b.unit.toFeet(b.value);

            double sumFeet=aFeet+bFeet;

            double converted=targetUnit.fromFeet(sumFeet);


            return roundToTwoDecimal(converted);
        }

    private static double roundToTwoDecimal(double value) {
        return Math.round(value*100.0)/100.0;

    }

    @Override
    public boolean equals(Object obj) {

        if (this==obj) return true;

        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other=(QuantityLength) obj;

        double thisFeet=this.unit.toFeet(this.value);

        double otherFeet=other.unit.toFeet(other.value);

        return Math.abs(thisFeet-otherFeet)<EPSILON;
    }



    @Override

    public int hashCode() {

        return Objects.hash(unit.toFeet(value));
    }

    @Override

    public String toString() {

        return "Quantity(" +value + ", "+unit+")";
    }
}





