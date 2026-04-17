package com.riddhi.quantity_service.controller;

import com.riddhi.quantity_service.dto.AddRequest;
import com.riddhi.quantity_service.dto.ArithmeticRequest;
import com.riddhi.quantity_service.dto.CompareRequest;
import com.riddhi.quantity_service.dto.ConvertRequest;
import com.riddhi.quantity_service.entity.QuantityOperation;
import com.riddhi.quantity_service.service.QuantityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController//api handler
@RequestMapping("/api/quantity" )//base url
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8080","https://quantity-measurement-app-frontend-puce.vercel.app"})
public class QuantityController {

    private final QuantityService quantityService;

    public QuantityController(QuantityService quantityService) {
        this.quantityService = quantityService;
    }

    // ── Health ─────────────────────────────────────────────────────────────
    @Tag(name = "Health")
    @Operation(summary = "Health check")
    @GetMapping("/test")
    public String test() { return "Quantity Service is running ✅"; }

    @Tag(name = "Health")
    @Operation(summary = "Health check (JSON)")
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "Quantity Service Running ✅", "port", 8081));
    }

    // ── CONVERT ─────────────────────────────────────────────────────────────
    @Tag(name = "Conversion")
    @Operation(
        summary = "Convert a unit value",
        description = """
            Convert a numeric value from one unit to another. **No authentication required.**
            
            When called with a valid JWT, the conversion is also saved to the operation history.
            
            **Example:** 5 FEET → METER = 1.524
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conversion successful",
            content = @Content(examples = @ExampleObject(value = """
                {"result":1.524,"from":"FEET","to":"METER","value":5.0,"operation":"CONVERT","category":"LENGTH"}
                """))),
        @ApiResponse(responseCode = "400", description = "Unknown unit or invalid input")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
        content = @Content(examples = {
            @ExampleObject(name = "Length", value = """
                {"value": 5, "fromUnit": "FEET", "toUnit": "METER"}"""),
            @ExampleObject(name = "Temperature", value = """
                {"value": 100, "fromUnit": "CELSIUS", "toUnit": "FAHRENHEIT"}"""),
            @ExampleObject(name = "Weight", value = """
                {"value": 70, "fromUnit": "KILOGRAM", "toUnit": "POUND"}""")
        }))
    @PostMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestBody ConvertRequest request,
            @Parameter(hidden = true) @RequestHeader(value = "X-User-Name", required = false) String username) {
        try {
            double result = quantityService.convert(request.getValue(), request.getFromUnit(), request.getToUnit());
            if (username != null && !username.isEmpty()) {
                quantityService.saveConvertOperation(request.getValue(), request.getFromUnit(), request.getToUnit(), result);
                //Agar JWT present hai → history save
                // Agar nahi → sirf result
            }
            return ResponseEntity.ok(Map.of(
                "result", result, "from", request.getFromUnit(),
                "to", request.getToUnit(), "value", request.getValue(),
                "operation", "CONVERT", "category", quantityService.getMeasurementType(request.getFromUnit())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── ARITHMETIC / ADD ────────────────────────────────────────────────────
    @Tag(name = "Arithmetic")
    @Operation(
        summary = "Add two quantities",
        description = "Add two quantities that may be in different (but compatible) units. Auto-converts to base unit, then returns result in `resultUnit`."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponse(responseCode = "200", description = "Addition result",
        content = @Content(examples = @ExampleObject(value = """
            {"result":3.5,"operation":"ADD","value1":1.0,"unit1":"FEET","value2":12.0,"unit2":"INCHES","resultUnit":"FEET"}
            """)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
        content = @Content(examples = @ExampleObject(value = """
            {"value1": 1, "unit1": "FEET", "value2": 12, "unit2": "INCHES", "resultUnit": "FEET"}
            """)))
    @PostMapping("/arithmetic/add")
    public ResponseEntity<?> arithmeticAdd(@RequestBody ArithmeticRequest req) {
        try {
            double result = quantityService.addArithmetic(
                    req.getValue1(), req.getUnit1(), req.getValue2(), req.getUnit2(), req.getResultUnit());
            return ResponseEntity.ok(Map.of(
                "result", result, "operation", "ADD",
                "value1", req.getValue1(), "unit1", req.getUnit1(),
                "value2", req.getValue2(), "unit2", req.getUnit2(),
                "resultUnit", req.getResultUnit() != null ? req.getResultUnit() : req.getUnit1()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── ARITHMETIC / SUBTRACT ───────────────────────────────────────────────
    @Tag(name = "Arithmetic")
    @Operation(summary = "Subtract two quantities", description = "Subtract Q2 from Q1 (auto-converts units).")
    @SecurityRequirement(name = "BearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
        content = @Content(examples = @ExampleObject(value = """
            {"value1": 2, "unit1": "METER", "value2": 50, "unit2": "CENTIMETER", "resultUnit": "METER"}
            """)))
    @PostMapping("/arithmetic/subtract")
    public ResponseEntity<?> arithmeticSubtract(@RequestBody ArithmeticRequest req) {
        try {
            double result = quantityService.subtractArithmetic(
                    req.getValue1(), req.getUnit1(), req.getValue2(), req.getUnit2(), req.getResultUnit());
            return ResponseEntity.ok(Map.of(
                "result", result, "operation", "SUBTRACT",
                "value1", req.getValue1(), "unit1", req.getUnit1(),
                "value2", req.getValue2(), "unit2", req.getUnit2(),
                "resultUnit", req.getResultUnit() != null ? req.getResultUnit() : req.getUnit1()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── ARITHMETIC / MULTIPLY ───────────────────────────────────────────────
    @Tag(name = "Arithmetic")
    @Operation(summary = "Multiply quantity by scalar", description = "Multiply a quantity by a plain number (no unit conversion).")
    @SecurityRequirement(name = "BearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
        content = @Content(examples = @ExampleObject(value = """
            {"value1": 5, "unit1": "METER", "scalar": 3}
            """)))
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

    // ── ARITHMETIC / DIVIDE ─────────────────────────────────────────────────
    @Tag(name = "Arithmetic")
    @Operation(summary = "Divide quantity by scalar", description = "Divide a quantity by a plain number.")
    @SecurityRequirement(name = "BearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
        content = @Content(examples = @ExampleObject(value = """
            {"value1": 100, "unit1": "KILOGRAM", "scalar": 4}
            """)))
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

    // ── ARITHMETIC / COMPARE ────────────────────────────────────────────────
    @Tag(name = "Arithmetic")
    @Operation(
        summary = "Compare two quantities",
        description = "Compare two quantities (even in different units) and returns `GREATER`, `LESS`, or `EQUAL`."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponse(responseCode = "200", description = "Comparison result",
        content = @Content(examples = @ExampleObject(value = """
            {"result":"GREATER","operation":"COMPARE","value1":2.0,"unit1":"METER","value2":100.0,"unit2":"CENTIMETER"}
            """)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
        content = @Content(examples = @ExampleObject(value = """
            {"value1": 2, "unit1": "METER", "value2": 100, "unit2": "CENTIMETER"}
            """)))
    @PostMapping("/arithmetic/compare")
    public ResponseEntity<?> compareArithmetic(@RequestBody ArithmeticRequest req) {
        try {
            String result = quantityService.compareArithmetic(
                    req.getValue1(), req.getUnit1(), req.getValue2(), req.getUnit2());
            return ResponseEntity.ok(Map.of(
                "result", result, "operation", "COMPARE",
                "value1", req.getValue1(), "unit1", req.getUnit1(),
                "value2", req.getValue2(), "unit2", req.getUnit2()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── Legacy endpoints ────────────────────────────────────────────────────
    @Tag(name = "Legacy (DTO-style)")
    @Operation(summary = "Add (legacy q1/q2 DTO)", description = "Legacy endpoint — prefer `/arithmetic/add`")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AddRequest request) {
        try {
            return ResponseEntity.ok(Map.of("result", quantityService.add(request.getQ1(), request.getQ2()), "operation", "ADD"));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @Tag(name = "Legacy (DTO-style)")
    @Operation(summary = "Subtract (legacy q1/q2 DTO)")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/subtract")
    public ResponseEntity<?> subtract(@RequestBody AddRequest request) {
        try {
            return ResponseEntity.ok(Map.of("result", quantityService.subtract(request.getQ1(), request.getQ2()), "operation", "SUBTRACT"));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @Tag(name = "Legacy (DTO-style)")
    @Operation(summary = "Compare (legacy q1/q2 DTO)")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/compare")
    public ResponseEntity<?> compare(@RequestBody CompareRequest request) {
        try {
            return ResponseEntity.ok(Map.of("result", quantityService.compare(request.getQ1(), request.getQ2()), "operation", "COMPARE"));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @Tag(name = "Legacy (DTO-style)")
    @Operation(summary = "Divide (legacy q1/q2 DTO)")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/divide")
    public ResponseEntity<?> divide(@RequestBody AddRequest request) {
        try {
            return ResponseEntity.ok(Map.of("result", quantityService.divide(request.getQ1(), request.getQ2()), "operation", "DIVIDE"));
        } catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    // ── CRUD ────────────────────────────────────────────────────────────────
    @Tag(name = "Operations History (DB)")
    @Operation(summary = "Get all saved operations", description = "Returns every saved quantity operation from DB.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/all")
    public List<QuantityOperation> getAll() { return quantityService.getAll(); }

    @Tag(name = "Operations History (DB)")
    @Operation(summary = "Get operation by ID")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try { return ResponseEntity.ok(quantityService.getById(id)); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }

    @Tag(name = "Operations History (DB)")
    @Operation(summary = "Delete operation by ID")
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try { return ResponseEntity.ok(Map.of("message", quantityService.deleteById(id))); }
        catch (Exception e) { return ResponseEntity.notFound().build(); }
    }
}
