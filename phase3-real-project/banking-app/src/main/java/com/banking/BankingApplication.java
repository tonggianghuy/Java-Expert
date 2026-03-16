package com.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Mini Banking Application - Phase 3 Real Project
 *
 * Features:
 * - User registration & login (JWT)
 * - Account management (Savings, Checking)
 * - Deposit, Withdraw, Transfer
 * - Transaction history & monthly statement
 * - Admin: freeze/unfreeze, statistics
 * - Audit logging
 *
 * Run: mvn spring-boot:run
 * Swagger: http://localhost:8080/swagger-ui.html
 * H2 Console: http://localhost:8080/h2-console
 */
@SpringBootApplication
public class BankingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
}
