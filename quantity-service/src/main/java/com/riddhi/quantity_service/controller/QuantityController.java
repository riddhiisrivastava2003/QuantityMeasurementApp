package com.riddhi.quantity_service.controller;

import com.riddhi.quantity_service.dto.AddRequest;
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

    // ✅ Health check
    @GetMapping("/test")
    public String test() {
        return "Quantity Service is running 🚀";
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "Quantity Service Running ✅"));
    }

    // ✅ GET /api/quantity/all - requires auth
    @GetMapping("/all")
    public List<QuantityOperation> getAll() {
        return quantityService.getAll();
    }

    // ✅ POST /api/quantity/convert - public endpoint
    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody ConvertRequest request,
                                      @RequestHeader(value = "X-User-Name", required = false) String username) {
        try {
            double result = quantityService.convert(
                    request.getValue(),
                    request.getFromUnit(),
                    request.getToUnit()
            );

            // Save to history only if user is authenticated
            if (username != null && !username.isEmpty()) {
                quantityService.saveConvertOperation(request.getValue(), request.getFromUnit(), request.getToUnit(), result);
            }

            return ResponseEntity.ok(Map.of(
                "result", result,
                "from", request.getFromUnit(),
                "to", request.getToUnit(),
                "value", request.getValue()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ ADD
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AddRequest request) {
        try {
            double result = quantityService.add(request.getQ1(), request.getQ2());
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ SUBTRACT
    @PostMapping("/subtract")
    public ResponseEntity<?> subtract(@RequestBody AddRequest request) {
        try {
            double result = quantityService.subtract(request.getQ1(), request.getQ2());
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ COMPARE
    @PostMapping("/compare")
    public ResponseEntity<?> compare(@RequestBody CompareRequest request) {
        try {
            boolean result = quantityService.compare(request.getQ1(), request.getQ2());
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ DIVIDE
    @PostMapping("/divide")
    public ResponseEntity<?> divide(@RequestBody AddRequest request) {
        try {
            double result = quantityService.divide(request.getQ1(), request.getQ2());
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(quantityService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("message", quantityService.deleteById(id)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
