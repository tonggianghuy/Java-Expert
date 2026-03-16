# NGÀY 9: Authentication & Security

---

## 1. AUTHENTICATION VS AUTHORIZATION

### 1.1 Phân biệt

```
Authentication (AuthN):  "BẠN LÀ AI?"
→ Xác minh danh tính (login bằng username/password)

Authorization (AuthZ):   "BẠN ĐƯỢC LÀM GÌ?"
→ Kiểm tra quyền hạn (admin xem tất cả, user xem của mình)
```

```
Ví dụ Banking:
1. AuthN: Khách hàng đăng nhập bằng username + password + OTP
2. AuthZ:
   - USER:   Xem tài khoản CỦA MÌNH, chuyển khoản từ TK của mình
   - TELLER: Xem tài khoản khách hàng, tạo tài khoản mới
   - ADMIN:  Freeze/unfreeze accounts, xem audit logs, quản lý users
```

### 1.2 Các phương pháp Authentication phổ biến

| Phương pháp | Mô tả | Dùng cho |
|-------------|-------|---------|
| **Session-based** | Server lưu session, client gửi cookie | Traditional web apps |
| **JWT (Token-based)** | Client giữ token, server không lưu state | REST API, SPA, Mobile |
| **OAuth 2.0** | Ủy quyền qua bên thứ 3 (Google, Facebook) | Social login |
| **API Key** | Key cố định cho mỗi client | Service-to-service |

---

## 2. JWT (JSON Web Token)

### 2.1 JWT là gì?

JWT = chuỗi mã hóa chứa thông tin user, được ký bằng secret key.

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNzA1MzA5NjAwfQ.abc123signature
     ↑ HEADER              ↑ PAYLOAD                                                        ↑ SIGNATURE
```

### 2.2 Cấu trúc JWT

```
HEADER (thuật toán + loại token)
{
  "alg": "HS256",
  "typ": "JWT"
}

PAYLOAD (data - KHÔNG đặt sensitive info ở đây!)
{
  "sub": "user1",          // Subject (user ID)
  "name": "Nguyen Van A",
  "role": "USER",
  "iat": 1705223200,       // Issued at
  "exp": 1705309600        // Expiration (24h)
}

SIGNATURE
HMACSHA256(
  base64(header) + "." + base64(payload),
  SECRET_KEY
)
```

> **⚠️ QUAN TRỌNG:** Payload chỉ được encode (Base64), KHÔNG được encrypt. Bất kỳ ai cũng có thể đọc payload. KHÔNG đặt password, SSN, card number trong JWT!

### 2.3 JWT Flow

```
1. Login:
Client                          Server
  │ POST /api/auth/login          │
  │ { username, password }   ──→  │ Verify credentials
  │                               │ Generate JWT
  │  ←── { token: "eyJ..." }     │

2. Subsequent requests:
Client                          Server
  │ GET /api/accounts             │
  │ Header: Authorization:        │
  │   Bearer eyJ...          ──→  │ Verify JWT signature
  │                               │ Extract user info
  │                               │ Check authorization
  │  ←── { accounts: [...] }     │

3. Token expired:
Client                          Server
  │ GET /api/accounts             │
  │ Header: Bearer eyJ...    ──→  │ JWT expired!
  │  ←── 401 Unauthorized        │
  │                               │
  │ POST /api/auth/login     ──→  │ Login lại
  │  ←── { token: "newToken" }   │
```

---

## 3. PASSWORD HASHING

### 3.1 Tại sao phải hash password?

```
❌ Lưu plain text:
username: "user1", password: "MyP@ssw0rd"
→ Database bị hack = LỘ TẤT CẢ passwords!

✅ Lưu hash:
username: "user1", password_hash: "$2a$12$LJ3m4ys..."
→ Database bị hack = Hacker KHÔNG THỂ đảo ngược hash
```

### 3.2 BCrypt

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);  // strength = 12

// Hash password (khi register)
String rawPassword = "MyP@ssw0rd";
String hashedPassword = encoder.encode(rawPassword);
// "$2a$12$LJ3m4ys6Gg7kJt..." (mỗi lần hash ra kết quả khác nhau - có salt)

// Verify password (khi login)
boolean matches = encoder.matches("MyP@ssw0rd", hashedPassword);  // true
boolean wrong = encoder.matches("WrongPass", hashedPassword);     // false
```

### 3.3 Quy tắc Password

```
KHÔNG BAO GIỜ:
✗ Lưu plain text password
✗ Dùng MD5 hoặc SHA1 (quá yếu, dễ brute force)
✗ Tự implement thuật toán hash
✗ Log password (kể cả trong error logs)

LUÔN LUÔN:
✓ Dùng BCrypt hoặc Argon2
✓ Salt đã tích hợp sẵn trong BCrypt
✓ Strength >= 10 (khuyên dùng 12)
```

---

## 4. SPRING SECURITY IMPLEMENTATION

### 4.1 Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

### 4.2 User Entity

```java
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private boolean active = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() { return passwordHash; }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return active; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return active; }
}

public enum Role {
    USER, TELLER, ADMIN
}
```

### 4.3 JWT Service

```java
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;  // 86400000 = 24 hours

    // Generate token
    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("role", user.getRole().name())
            .claim("userId", user.getId())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(getSigningKey())
            .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return resolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

### 4.4 Auth Controller

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse auth = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(auth));
    }
}

// DTOs
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 8, message = "Mật khẩu ít nhất 8 ký tự")
    private String password;

    @NotBlank
    @Email
    private String email;
}

public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private long expiresIn;
    private UserResponse user;
}
```

### 4.5 Auth Service

```java
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest request) {
        // Check username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username đã tồn tại");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);

        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        // Spring Security authenticate
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setExpiresIn(86400);  // 24h
        response.setUser(UserMapper.toResponse(user));
        return response;
    }
}
```

### 4.6 Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Enable @PreAuthorize
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF cho REST API
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/products/**").permitAll()  // Xem SP không cần login
                .requestMatchers("/h2-console/**").permitAll()
                // Admin only
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Authenticated
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

### 4.7 JWT Filter

```java
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract token
        String token = authHeader.substring(7);

        // 3. Extract username từ token
        String username = jwtService.extractUsername(token);

        // 4. Load user và validate token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                // 5. Set authentication vào SecurityContext
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

---

## 5. ROLE-BASED ACCESS CONTROL (RBAC)

### 5.1 @PreAuthorize

```java
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    // Chỉ USER đã đăng nhập
    @GetMapping("/my-accounts")
    @PreAuthorize("hasRole('USER')")
    public List<AccountResponse> getMyAccounts(
            @AuthenticationPrincipal User user) {  // Lấy user hiện tại
        return accountService.getAccountsByUserId(user.getId());
    }

    // Chỉ ADMIN
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // ADMIN hoặc TELLER
    @PostMapping("/{id}/freeze")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public AccountResponse freezeAccount(@PathVariable Long id) {
        return accountService.freezeAccount(id);
    }

    // Chuyển khoản: kiểm tra user sở hữu tài khoản
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public TransferResponse transfer(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransferRequest request) {
        // Service sẽ verify user sở hữu fromAccount
        return transferService.transfer(user.getId(), request);
    }
}
```

---

## 6. AUDIT LOGGING

### 6.1 Audit Log Entity

```java
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String username;
    private String action;          // LOGIN, TRANSFER, CREATE_ACCOUNT, FREEZE_ACCOUNT
    private String resourceType;    // ACCOUNT, TRANSACTION, USER
    private String resourceId;
    private String details;         // JSON chi tiết (KHÔNG chứa sensitive data)
    private String ipAddress;
    private String status;          // SUCCESS, FAILED
    private String failReason;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
```

### 6.2 Audit Service

```java
@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(String action, String resourceType, String resourceId,
                   String details, String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setResourceType(resourceType);
        log.setResourceId(resourceId);
        log.setDetails(details);
        log.setStatus(status);

        if (auth != null && auth.getPrincipal() instanceof User user) {
            log.setUserId(user.getId());
            log.setUsername(user.getUsername());
        }

        auditLogRepository.save(log);
    }
}

// Sử dụng trong TransferService:
@Transactional
public TransferResponse transfer(Long userId, TransferRequest request) {
    try {
        // ... transfer logic ...
        auditService.log("TRANSFER", "ACCOUNT", request.getFromAccount(),
            String.format("Transfer %s to %s, amount: %s",
                request.getFromAccount(), request.getToAccount(), request.getAmount()),
            "SUCCESS");
        return response;
    } catch (Exception e) {
        auditService.log("TRANSFER", "ACCOUNT", request.getFromAccount(),
            e.getMessage(), "FAILED");
        throw e;
    }
}
```

---

## 7. CORS CONFIGURATION

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));  // Frontend URL
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
```

---

## 8. TÓM TẮT NGÀY 9

```
┌──────────────────────────────────────────────────┐
│               NGÀY 9 - TÓM TẮT                  │
├──────────────────────────────────────────────────┤
│ Authentication vs Authorization:                 │
│ ✓ AuthN = "Bạn là ai?" (login)                  │
│ ✓ AuthZ = "Bạn được làm gì?" (roles)           │
│                                                  │
│ JWT:                                             │
│ ✓ Header.Payload.Signature                       │
│ ✓ Stateless - server không lưu session           │
│ ✓ KHÔNG đặt sensitive data trong payload         │
│                                                  │
│ Password:                                        │
│ ✓ BCrypt hash (strength >= 12)                   │
│ ✓ KHÔNG lưu plain text, KHÔNG dùng MD5          │
│                                                  │
│ Spring Security:                                 │
│ ✓ SecurityFilterChain configuration              │
│ ✓ JwtAuthFilter                                  │
│ ✓ @PreAuthorize cho RBAC                        │
│ ✓ @AuthenticationPrincipal lấy current user     │
│                                                  │
│ Audit:                                           │
│ ✓ Log mọi thao tác: WHO, WHAT, WHEN, RESULT    │
│                                                  │
│ ⚠️ JWT payload = Base64 encode, NOT encrypted   │
│ ⚠️ Luôn verify user owns resource trước khi cho │
│    truy cập                                      │
│ ⚠️ CORS config cho frontend communication       │
└──────────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. Authentication và Authorization khác nhau thế nào?
2. JWT gồm những phần nào? Tại sao không đặt password trong JWT?
3. Tại sao dùng BCrypt mà không dùng MD5?
4. `@PreAuthorize("hasRole('ADMIN')")` hoạt động thế nào?
5. Khi nào token bị reject? (liệt kê các trường hợp)
6. Audit logging quan trọng thế nào trong Banking?
