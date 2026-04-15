package com.stayease.property_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.stayease")
@Slf4j
public class PropertyServiceApplication {

	public static void main(String[] args) {
		log.info("Starting Property Service Application");
		SpringApplication.run(PropertyServiceApplication.class, args);
		log.info("Property Service Application Started Successfully on port 8084");
		log.info("API Documentation: http://localhost:8084/swagger-ui.html");
	}

}
