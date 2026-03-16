package com.banking.controller;

import com.banking.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Ngày 9 - Auth Controller
 *
 * Endpoints:
 *   POST /api/auth/register  → Đăng ký
 *   POST /api/auth/login     → Đăng nhập, nhận JWT token
 *
 * TODO: Implement AuthService với:
 *   1. UserRepository.save() cho register
 *   2. BCryptPasswordEncoder.encode() cho hash password
 *   3. AuthenticationManager.authenticate() cho login
 *   4. JwtService.generateToken() cho tạo JWT
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // TODO: Inject AuthService
    // private final AuthService authService;

    /**
     * POST /api/auth/register
     *
     * Request:
     * {
     *   "username": "nguyenvana",
     *   "password": "MyP@ssw0rd",
     *   "email": "a@email.com"
     * }
     *
     * Response 201:
     * {
     *   "success": true,
     *   "data": { "id": 1, "username": "nguyenvana", "email": "a@email.com" },
     *   "message": "Đăng ký thành công"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // TODO: Implement
        // 1. Check username/email chưa tồn tại
        // 2. Hash password bằng BCrypt
        // 3. Save user
        // 4. Return user info (KHÔNG trả password!)

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("TODO: Implement register");
    }

    /**
     * POST /api/auth/login
     *
     * Request:
     * {
     *   "username": "nguyenvana",
     *   "password": "MyP@ssw0rd"
     * }
     *
     * Response 200:
     * {
     *   "token": "eyJhbGciOiJIUzI1NiJ9...",
     *   "tokenType": "Bearer",
     *   "expiresIn": 86400,
     *   "username": "nguyenvana",
     *   "role": "USER"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // TODO: Implement
        // 1. AuthenticationManager.authenticate(username, password)
        // 2. Nếu thành công → JwtService.generateToken(user)
        // 3. Return AuthResponse với token
        // 4. Nếu thất bại → 401 Unauthorized

        return ResponseEntity.ok("TODO: Implement login");
    }
}
