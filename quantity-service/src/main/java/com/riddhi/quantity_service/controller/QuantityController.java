package com.riddhi.quantity_service.controller;

import com.riddhi.quantity_service.service.QuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quantity")
public class QuantityController {

    @Autowired
    private QuantityService quantityService;

    @GetMapping("/test")
    public String test() {
        return "Quantity Service is running 🚀";
    }

    @GetMapping("/all")
    public String getAll() {
        return quantityService.getAll();
    }

    @PostMapping("/add")
    public String addQuantity(@RequestBody String quantity) {
        return quantityService.addQuantity(quantity);
    }
}