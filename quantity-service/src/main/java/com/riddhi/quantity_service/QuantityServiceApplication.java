package com.riddhi.quantity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


// ✅ Correct annotation
  @EnableFeignClients
@SpringBootApplication
public class QuantityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantityServiceApplication.class, args);
	}
}