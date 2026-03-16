package com.banking.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Ngày 8 - Request DTO cho chuyển khoản
 */
public class TransferRequest {

    @NotBlank(message = "Số tài khoản gửi không được trống")
    private String fromAccount;

    @NotBlank(message = "Số tài khoản nhận không được trống")
    private String toAccount;

    @NotNull(message = "Số tiền không được null")
    @DecimalMin(value = "1000", message = "Số tiền chuyển tối thiểu 1,000 VND")
    @DecimalMax(value = "500000000", message = "Số tiền chuyển tối đa 500,000,000 VND/lần")
    private BigDecimal amount;

    private String description;

    // === Constructors ===
    public TransferRequest() {}

    public TransferRequest(String fromAccount, String toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    // === Getters & Setters ===
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
