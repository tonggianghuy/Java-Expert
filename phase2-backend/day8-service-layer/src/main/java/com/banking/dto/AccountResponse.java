package com.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ngày 8 - Response DTO cho Account
 *
 * KHÔNG chứa: password, userId internal, transactions list đầy đủ
 */
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private String accountType;
    private boolean active;
    private LocalDateTime createdAt;

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
