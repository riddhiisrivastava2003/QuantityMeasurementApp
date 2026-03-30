package com.riddhi.quantity_service.service;

import org.springframework.stereotype.Service;

@Service
public class QuantityService {

    public String getAll() {
        return "All quantities from Service Layer ✅";
    }

    public String addQuantity(String quantity) {
        return "Quantity added from Service: " + quantity;
    }
}