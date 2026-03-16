package com.banking.dto;

import jakarta.validation.constraints.*;

/**
 * Ngày 9 - DTO cho đăng ký tài khoản
 */
public class RegisterRequest {

    @NotBlank(message = "Username không được trống")
    @Size(min = 3, max = 50, message = "Username từ 3-50 ký tự")
    private String username;

    @NotBlank(message = "Password không được trống")
    @Size(min = 8, message = "Password ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Email không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    // === Getters & Setters ===
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
