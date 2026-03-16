package com.banking.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Ngày 9 - DTO cho đăng nhập
 */
public class LoginRequest {

    @NotBlank(message = "Username không được trống")
    private String username;

    @NotBlank(message = "Password không được trống")
    private String password;

    // === Getters & Setters ===
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
