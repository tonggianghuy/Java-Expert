package com.ecommerce.dto;

import java.time.LocalDateTime;

/**
 * Ngày 6 - Consistent API Response wrapper
 *
 * Tất cả API endpoints đều trả về format này:
 * {
 *   "success": true/false,
 *   "data": { ... },
 *   "message": "...",
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 */
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private LocalDateTime timestamp;

    // === Constructors ===

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, T data, String message) {
        this();
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // === Static Factory Methods (gọi nhanh) ===

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "OK");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }

    // === Getters & Setters ===

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
