package com.riddhi.quantity_service.controller;

import com.riddhi.quantity_service.dto.AddRequest;
import com.riddhi.quantity_service.dto.CompareRequest;
import com.riddhi.quantity_service.dto.ConvertRequest;
import com.riddhi.quantity_service.entity.QuantityOperation;
import com.riddhi.quantity_service.service.QuantityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/quantity")
public class QuantityController {

    private final QuantityService quantityService;

    public QuantityController(QuantityService quantityService) {
        this.quantityService = quantityService;
    }

    @GetMapping("/test")
    public String test() {
        return "Quantity Service is running 🚀";
    }



    @GetMapping("/all")
    public List<QuantityOperation> getAll() {
        return quantityService.getAll();
    }


    @PostMapping("/convert")
    public double convert(@RequestBody ConvertRequest request) {
        return quantityService.convert(
                request.getValue(),
                request.getFromUnit(),
                request.getToUnit()
        );
    }

    @PostMapping("/add")
    public double add(@RequestBody AddRequest request) {
        return quantityService.add(
                request.getQ1(),
                request.getQ2()
        );
    }

    @PostMapping("/compare")
    public boolean compare(@RequestBody CompareRequest request) {
        return quantityService.compare(
                request.getQ1(),
                request.getQ2()
        );
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return quantityService.deleteById(id);
    }


    @GetMapping("/{id}")
    public QuantityOperation getById(@PathVariable Long id) {
        return quantityService.getById(id);
    }

    @PostMapping("/subtract")
    public double subtract(@RequestBody AddRequest request) {
        return quantityService.subtract(
                request.getQ1(),
                request.getQ2()
        );
    }

    @PostMapping("/divide")
    public double divide(@RequestBody AddRequest request) {
        return quantityService.divide(
                request.getQ1(),
                request.getQ2()
        );
    }
}