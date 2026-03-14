//package com.bridgelabz.standalone_unit;
//
//
//
//public class QuantityMeasurementApp {
//
//    public static void main(String[] args) {
//
//
//        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
//
//
//        System.out.println(q1.convertTo(LengthUnit.INCHES));
//
//        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);
//
//
//        System.out.println(q1.add(q2, LengthUnit.FEET));
//
//
//        QuantityLength q3 = new QuantityLength(36.0, LengthUnit.INCHES);
//
//
//        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.YARDS);
//
//        System.out.println(q3.equals(q4));
//    }
//}

package com.bridgelabz.standalone_unit;

public class QuantityMeasurementApp {

    public static void main(String[] args) {


        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println(q1.convertTo(LengthUnit.INCHES));

        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);
        System.out.println(q1.add(q2, LengthUnit.FEET));

        QuantityLength q3 = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.YARDS);
        System.out.println(q3.equals(q4));



        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);


        System.out.println(w1.equals(w2));


        System.out.println(w1.convertTo(WeightUnit.POUND));

        System.out.println(w1.add(w2));


        System.out.println(w1.add(w2, WeightUnit.GRAM));

        System.out.println(w1.equals(q1));
    }
}