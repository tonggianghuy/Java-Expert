package com.banking.dto;

/**
 * Ngày 9 - Response DTO cho login thành công
 */
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private long expiresIn;  // seconds
    private String username;
    private String role;

    // === Constructors ===
    public AuthResponse() {}

    public AuthResponse(String token, long expiresIn, String username, String role) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
        this.role = role;
    }

    // === Getters & Setters ===
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }

    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
