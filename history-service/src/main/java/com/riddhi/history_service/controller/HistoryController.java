package com.riddhi.history_service.controller;

import com.riddhi.history_service.entity.OperationHistory;
import com.riddhi.history_service.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8080"})
@Tag(name = "History", description = "Conversion & operation history for authenticated users")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "History Service Running ✅", "port", 8083));
    }

    @Operation(
        summary = "Save a history entry",
        description = "Called automatically by the frontend after every authenticated conversion or arithmetic operation."
    )
    @SecurityRequirement(name = "BearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
        content = @Content(examples = @ExampleObject(value = """
            {
              "username": "john_doe",
              "operation": "CONVERT",
              "fromUnit": "FEET",
              "toUnit": "METER",
              "inputValue": 5.0,
              "result": "1.524",
              "measurementType": "LENGTH"
            }
            """)))
    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestBody Map<String, Object> data,
            @Parameter(hidden = true) @RequestHeader(value = "X-User-Name", required = false) String xUsername) {
        try {
            if (!data.containsKey("username") && xUsername != null) {
                data.put("username", xUsername);
            }
            OperationHistory saved = historyService.save(data);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Get my history",
        description = "Returns all operations for the currently authenticated user, newest first."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponse(responseCode = "200", description = "List of history entries")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping("/my")
    public ResponseEntity<?> getMyHistory(
            @Parameter(hidden = true) @RequestHeader(value = "X-User-Name", required = false) String xUsername,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String username = resolveUsername(xUsername, authHeader);
            if (username == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            return ResponseEntity.ok(historyService.getByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get history by username", description = "Fetch history for a specific username (admin use).")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(historyService.getByUsername(username));
    }

    @Operation(
        summary = "Clear my history",
        description = "Permanently deletes all history records for the current user."
    )
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearHistory(
            @Parameter(hidden = true) @RequestHeader(value = "X-User-Name", required = false) String xUsername,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String username = resolveUsername(xUsername, authHeader);
            if (username == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            historyService.clearByUsername(username);
            return ResponseEntity.ok(Map.of("message", "History cleared ✅"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all history (admin)", description = "Returns every history record in the database.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(historyService.getAll());
    }

    private String resolveUsername(String xUsername, String authHeader) {
        if (xUsername != null && !xUsername.isEmpty()) return xUsername;
        return null;
    }
}
