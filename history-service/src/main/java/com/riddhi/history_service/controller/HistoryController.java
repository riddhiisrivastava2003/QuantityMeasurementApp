package com.riddhi.history_service.controller;

import com.riddhi.history_service.entity.OperationHistory;
import com.riddhi.history_service.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8080"})
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // ✅ Health check
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "History Service Running ✅"));
    }

    // ✅ Save a new history entry (called by quantity-service or frontend via gateway)
    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestBody Map<String, Object> data,
            @RequestHeader(value = "X-User-Name", required = false) String xUsername) {
        try {
            // Use header username if not in body
            if (!data.containsKey("username") && xUsername != null) {
                data.put("username", xUsername);
            }
            OperationHistory saved = historyService.save(data);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get history for authenticated user
    @GetMapping("/my")
    public ResponseEntity<?> getMyHistory(
            @RequestHeader(value = "X-User-Name", required = false) String xUsername,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String username = resolveUsername(xUsername, authHeader);
            if (username == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            return ResponseEntity.ok(historyService.getByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get history by username (admin use or direct)
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(historyService.getByUsername(username));
    }

    // ✅ Clear all history for authenticated user
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearHistory(
            @RequestHeader(value = "X-User-Name", required = false) String xUsername,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String username = resolveUsername(xUsername, authHeader);
            if (username == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            historyService.clearByUsername(username);
            return ResponseEntity.ok(Map.of("message", "History cleared ✅"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get all history (admin)
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(historyService.getAll());
    }

    private String resolveUsername(String xUsername, String authHeader) {
        if (xUsername != null && !xUsername.isEmpty()) return xUsername;
        return null;
    }
}
