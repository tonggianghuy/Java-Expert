package com.banking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Ngày 9 - Audit Log Entity
 *
 * Ghi lại MỌI thao tác quan trọng trong hệ thống.
 * Bắt buộc trong Banking: WHO did WHAT, WHEN, and the RESULT.
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_user", columnList = "user_id"),
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_created", columnList = "created_at")
})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String action;           // LOGIN, TRANSFER, CREATE_ACCOUNT, FREEZE_ACCOUNT...

    @Column(name = "resource_type", length = 30)
    private String resourceType;     // ACCOUNT, TRANSACTION, USER

    @Column(name = "resource_id", length = 50)
    private String resourceId;

    @Column(columnDefinition = "TEXT")
    private String details;          // JSON hoặc text mô tả (KHÔNG chứa sensitive data!)

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(nullable = false, length = 20)
    private String status;           // SUCCESS, FAILED

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // === Constructors ===

    public AuditLog() {}

    public static AuditLog success(String action, String resourceType,
                                   String resourceId, String details) {
        AuditLog log = new AuditLog();
        log.action = action;
        log.resourceType = resourceType;
        log.resourceId = resourceId;
        log.details = details;
        log.status = "SUCCESS";
        return log;
    }

    public static AuditLog failure(String action, String resourceType,
                                   String resourceId, String reason) {
        AuditLog log = new AuditLog();
        log.action = action;
        log.resourceType = resourceType;
        log.resourceId = resourceId;
        log.status = "FAILED";
        log.failReason = reason;
        return log;
    }

    // === Getters & Setters ===

    public Long getId() { return id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
