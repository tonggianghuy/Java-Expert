package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ngày 6 - Entry point cho Spring Boot application
 *
 * Chạy: mvn spring-boot:run
 * Hoặc: Run class này trong IntelliJ
 *
 * Server sẽ khởi động tại http://localhost:8080
 */
@SpringBootApplication
public class ProductApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApiApplication.class, args);
        System.out.println("\n🚀 Product API is running at http://localhost:8080");
        System.out.println("📝 Test with Postman or Thunder Client\n");
    }
}
