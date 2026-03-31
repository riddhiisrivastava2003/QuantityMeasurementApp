//package com.riddhi.quantity_service.service;
//
//import com.riddhi.quantity_service.dto.Quantity;
//import com.riddhi.quantity_service.dto.QuantityDTO;
//import com.riddhi.quantity_service.repository.QuantityRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//
//@Service
//
//public class QuantityService{
//
//    @Autowired
//    private QuantityRepository repository;
//
//    public String getAll() {
//        return "All quantities from Service Layer ✅";
//    }
//
//    public double subtract(QuantityDTO q1, QuantityDTO q2) {
//
//        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
//        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");
//
//        return v1 - v2;
//    }
//
//
//
//    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
//
//        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
//        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");
//
//        return v1 == v2;
//    }
//
//    public double add(QuantityDTO q1, QuantityDTO q2) {
//
//        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
//        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");
//
//        double sumInInches = v1 + v2;
//
//        // result ko FEET me return kar rahe hain
//        return convert(sumInInches, "INCHES", "FEET");
//    }
//
//    public double divide(QuantityDTO q1, QuantityDTO q2) {
//
//        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
//        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");
//
//        if (v2 == 0) {
//            throw new RuntimeException("Cannot divide by zero");
//        }
//
//        return v1 / v2;
//    }
//
//    public double convert(double value, String from, String to) {
//
//        // Normalize input (VERY IMPORTANT)
//        from = from.toUpperCase();
//        to = to.toUpperCase();
//
//        double valueInInch;
//
//        // Step 1: Convert to base (INCH)
//        switch (from) {
//            case "FEET":
//                valueInInch = value * 12;
//                break;
//            case "INCHES":
//                valueInInch = value;
//                break;
//            case "YARD":
//                valueInInch = value * 36;
//                break;
//            default:
//                throw new RuntimeException("Invalid FROM unit: " + from);
//        }
//
//        // Step 2: Convert to target
//        switch (to) {
//            case "FEET":
//                return valueInInch / 12;
//            case "INCHES":
//                return valueInInch;
//            case "YARD":
//                return valueInInch / 36;
//            default:
//                throw new RuntimeException("Invalid TO unit: " + to);
//        }
//    }
//}


package com.riddhi.quantity_service.service;

import com.riddhi.quantity_service.dto.QuantityDTO;
import com.riddhi.quantity_service.entity.QuantityOperation;
import com.riddhi.quantity_service.exceptions.ResourceNotFoundException;
import com.riddhi.quantity_service.repository.QuantityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityService {

    private final QuantityRepository repository;

    public QuantityService(QuantityRepository repository) {
        this.repository = repository;
    }

    public double add(QuantityDTO q1, QuantityDTO q2) {

        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");

        double result = convert(v1 + v2, "INCHES", "FEET");

        saveToDB("ADD", q1, q2, result);

        return result;
    }

    public double subtract(QuantityDTO q1, QuantityDTO q2) {

        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");

        double result = v1 - v2;

        saveToDB("SUBTRACT", q1, q2, result);

        return result;
    }

    public boolean compare(QuantityDTO q1, QuantityDTO q2) {

        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");

        boolean result = v1 == v2;

        saveToDB("COMPARE", q1, q2, result ? 1 : 0);

        return result;
    }

    public double divide(QuantityDTO q1, QuantityDTO q2) {

        double v1 = convert(q1.getValue(), q1.getUnit(), "INCHES");
        double v2 = convert(q2.getValue(), q2.getUnit(), "INCHES");

        if (v2 == 0) throw new RuntimeException("Divide by zero");

        double result = v1 / v2;

        saveToDB("DIVIDE", q1, q2, result);

        return result;
    }

    // 🔥 COMMON SAVE METHOD
    private void saveToDB(String operation, QuantityDTO q1, QuantityDTO q2, double result) {

        QuantityOperation op = new QuantityOperation();

        op.setOperation(operation);
        op.setMeasurementType("LENGTH");

        op.setValue1(q1.getValue());
        op.setUnit1(q1.getUnit());

        op.setValue2(q2.getValue());
        op.setUnit2(q2.getUnit());

        op.setResult(result);

        repository.save(op);
    }

    public List<QuantityOperation> getAll() {
        return repository.findAll();
    }

    public QuantityOperation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data not found with id: " + id));
    }

    public String deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Data not found with id: " + id);
        }

        repository.deleteById(id);
        return "Deleted successfully ✅";
    }

    // 🔁 CONVERSION LOGIC
    public double convert(double value, String from, String to) {

        from = from.toUpperCase();
        to = to.toUpperCase();

        double valueInInch;

        switch (from) {
            case "FEET": valueInInch = value * 12; break;
            case "INCHES": valueInInch = value; break;
            case "YARD": valueInInch = value * 36; break;
            default: throw new RuntimeException("Invalid unit");
        }

        switch (to) {
            case "FEET": return valueInInch / 12;
            case "INCHES": return valueInInch;
            case "YARD": return valueInInch / 36;
            default: throw new RuntimeException("Invalid unit");
        }
    }
}