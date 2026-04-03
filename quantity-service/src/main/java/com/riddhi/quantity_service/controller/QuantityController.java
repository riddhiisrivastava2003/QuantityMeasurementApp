package com.riddhi.quantity_service.controller;

import com.riddhi.quantity_service.dto.AddRequest;
import com.riddhi.quantity_service.dto.ArithmeticRequest;
import com.riddhi.quantity_service.dto.CompareRequest;
import com.riddhi.quantity_service.dto.ConvertRequest;
import com.riddhi.quantity_service.entity.QuantityOperation;
import com.riddhi.quantity_service.service.QuantityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quantity")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8080"})
public class QuantityController {

    private final QuantityService quantityService;

    public QuantityController(QuantityService quantityService) {
        this.quantityService = quantityService;
    }

    // ── Health ────────────────────────────────────────────────────────────
    @GetMapping("/test")
    public String test() { return "Quantity Service is running"; }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "Quantity Service Running"));
    }

    // ── CONVERT (public) ─────────────────────────────────────────────────
    @PostMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestBody ConvertRequest request,
            @RequestHeader(value = "X-User-Name", required = false) String username) {
        try {
            double result = quantityService.convert(
                    request.getValue(), request.getFromUnit(), request.getToUnit());

            if (username != null && !username.isEmpty()) {
                quantityService.saveConvertOperation(
                        request.getValue(), request.getFromUnit(), request.getToUnit(), result);
            }

            return ResponseEntity.ok(Map.of(
                "result",      result,
                "from",        request.getFromUnit(),
                "to",          request.getToUnit(),
                "value",       request.getValue(),
                "operation",   "CONVERT",
                "category",    quantityService.getMeasurementType(request.getFromUnit())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── ARITHMETIC — ADD ──────────────────────────────────────────────────
    // Old DTO-style (q1/q2 objects) — kept for backward compat
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AddRequest request) {
        try {
            return ResponseEntity.ok(Map.of(
                "result", quantityService.add(request.getQ1(), request.getQ2()),
                "operation", "ADD"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // New flat-style arithmetic (value1, unit1, value2, unit2, resultUnit)
    @PostMapping("/arithmetic/add")
    public ResponseEntity<?> arithmeticAdd(@RequestBody ArithmeticRequest req) {
        try {
            double result = quantityService.addArithmetic(
                    req.getValue1(), req.getUnit1(), req.getValue2(), req.getUnit2(), req.getResultUnit());
            return ResponseEntity.ok(Map.of(
                "result",    result,
                "operation", "ADD",
                "value1",    req.getValue1(),
                "unit1",     req.getUnit1(),
                "value2",    req.getValue2(),
                "unit2",     req.getUnit2(),
                "resultUnit", req.getResultUnit() != null ? req.getResultUnit() : req.getUnit1()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── SUBTRACT ─────────────────────────────────────────────────────────
    @PostMapping("/subtract")
    public ResponseEntity<?> subtract(@RequestBody AddRequest request) {
        try {
            return ResponseEntity.ok(Map.of(
                "result", quantityService.subtract(request.getQ1(), request.getQ2()),
                "operation", "SUBTRACT"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/arithmetic/subtract")
    public ResponseEntity<?> arithmeticSubtract(@RequestBody ArithmeticRequest req) {
        try {
            double result = quantityService.subtractArithmetic(
                    req.getValue1(), req.getUnit1(), req.getValue2(), req.getUnit2(), req.getResultUnit());
            return ResponseEntity.ok(Map.of(
                "result",    result,
                "operation", "SUBTRACT",
                "value1",    req.getValue1(), "unit1", req.getUnit1(),
                "value2",    req.getValue2(), "unit2", req.getUnit2(),
                "resultUnit", req.getResultUnit() != null ? req.getResultUnit() : req.getUnit1()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── MULTIPLY (scalar) ─────────────────────────────────────────────────
    @PostMapping("/arithmetic/multiply")
    public ResponseEntity<?> multiply(@RequestBody Map<String, Object> body) {
        try {
            double value1 = ((Number) body.get("value1")).doubleValue();
            String unit1  = (String) body.get("unit1");
            double scalar = ((Number) body.get("scalar")).doubleValue();
            double result = quantityService.multiply(value1, unit1, scalar);
            return ResponseEntity.ok(Map.of(
                "result", result, "operation", "MULTIPLY",
                "value1", value1, "unit1", unit1, "scalar", scalar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── DIVIDE (scalar) ───────────────────────────────────────────────────
    @PostMapping("/divide")
    public ResponseEntity<?> divide(@RequestBody AddRequest request) {
        try {
            return ResponseEntity.ok(Map.of(
                "result", quantityService.divide(request.getQ1(), request.getQ2()),
                "operation", "DIVIDE"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/arithmetic/divide")
    public ResponseEntity<?> divideScalar(@RequestBody Map<String, Object> body) {
        try {
            double value1 = ((Number) body.get("value1")).doubleValue();
            String unit1  = (String) body.get("unit1");
            double scalar = ((Number) body.get("scalar")).doubleValue();
            double result = quantityService.divideScalar(value1, unit1, scalar);
            return ResponseEntity.ok(Map.of(
                "result", result, "operation", "DIVIDE",
                "value1", value1, "unit1", unit1, "scalar", scalar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── COMPARE ───────────────────────────────────────────────────────────
    @PostMapping("/compare")
    public ResponseEntity<?> compare(@RequestBody CompareRequest request) {
        try {
            return ResponseEntity.ok(Map.of(
                "result", quantityService.compare(request.getQ1(), request.getQ2()),
                "operation", "COMPARE"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/arithmetic/compare")
    public ResponseEntity<?> compareArithmetic(@RequestBody ArithmeticRequest req) {
        try {
            String result = quantityService.compareArithmetic(
                    req.getValue1(), req.getUnit1(), req.getValue2(), req.getUnit2());
            return ResponseEntity.ok(Map.of(
                "result",    result,
                "operation", "COMPARE",
                "value1",    req.getValue1(), "unit1", req.getUnit1(),
                "value2",    req.getValue2(), "unit2", req.getUnit2()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── CRUD ─────────────────────────────────────────────────────────────
    @GetMapping("/all")
    public List<QuantityOperation> getAll() {
        return quantityService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try { return ResponseEntity.ok(quantityService.getById(id)); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try { return ResponseEntity.ok(Map.of("message", quantityService.deleteById(id))); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }
}
