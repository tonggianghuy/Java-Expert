package com.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ngày 8 - Response DTO cho chuyển khoản
 *
 * Chỉ chứa thông tin client CẦN thấy.
 * KHÔNG chứa: internal IDs, password hashes, full account details.
 */
public class TransferResponse {

    private String status;            // SUCCESS | FAILED
    private String transactionId;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal senderBalanceAfter;
    private String fromAccount;       // Masked: ****1001
    private String toAccount;         // Masked: ****1002
    private String message;
    private LocalDateTime timestamp;

    // === Static factory ===
    public static TransferResponse success(String txId, BigDecimal amount, BigDecimal fee,
                                           BigDecimal senderBalance, String from, String to) {
        TransferResponse r = new TransferResponse();
        r.status = "SUCCESS";
        r.transactionId = txId;
        r.amount = amount;
        r.fee = fee;
        r.senderBalanceAfter = senderBalance;
        r.fromAccount = maskAccount(from);
        r.toAccount = maskAccount(to);
        r.message = "Chuyển khoản thành công";
        r.timestamp = LocalDateTime.now();
        return r;
    }

    private static String maskAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    // === Getters & Setters ===
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public BigDecimal getSenderBalanceAfter() { return senderBalanceAfter; }
    public void setSenderBalanceAfter(BigDecimal senderBalanceAfter) { this.senderBalanceAfter = senderBalanceAfter; }

    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
