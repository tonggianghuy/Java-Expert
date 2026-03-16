# NGÀY 6: Spring Boot - Khởi động Backend

---

## 1. SPRING BOOT LÀ GÌ?

### 1.1 Lịch sử ngắn

```
Spring Framework (2003)     → Mạnh nhưng config phức tạp (XML hell)
        ↓
Spring Boot (2014)          → "Convention over Configuration"
                               Auto-configuration, embedded server
                               Chạy ngay, config ít nhất
```

### 1.2 Tại sao Banking dùng Spring Boot?

| Lý do | Chi tiết |
|-------|---------|
| **Mature** | 20+ năm, enterprise-proven |
| **Security** | Spring Security - authentication/authorization built-in |
| **Transaction** | `@Transactional` - đảm bảo ACID cho giao dịch tài chính |
| **Ecosystem** | Spring Data JPA, Spring Cloud, Spring Batch... |
| **Community** | Cộng đồng lớn, tài liệu phong phú |

### 1.3 Kiến trúc Spring Boot

```
Client (Browser/App)
       │
       ▼ HTTP Request
┌──────────────────────────────────────────┐
│ Spring Boot Application                   │
│                                           │
│  ┌─────────────┐                         │
│  │ Controller   │ ← Nhận request         │
│  │ @RestController                       │
│  └──────┬──────┘                         │
│         │                                │
│  ┌──────▼──────┐                         │
│  │ Service      │ ← Business logic       │
│  │ @Service     │                        │
│  └──────┬──────┘                         │
│         │                                │
│  ┌──────▼──────┐                         │
│  │ Repository   │ ← Database access      │
│  │ @Repository  │                        │
│  └──────┬──────┘                         │
│         │                                │
│  ┌──────▼──────┐                         │
│  │ Database     │                        │
│  └─────────────┘                         │
└──────────────────────────────────────────┘
       │
       ▼ HTTP Response (JSON)
Client
```

---

## 2. TẠO PROJECT

### 2.1 Spring Initializr

1. Vào https://start.spring.io
2. Chọn:
   - Project: **Maven**
   - Language: **Java**
   - Spring Boot: **3.2.x** (latest stable)
   - Group: `com.banking`
   - Artifact: `banking-api`
   - Java: **17**
3. Dependencies:
   - **Spring Web** (REST API)
   - **Spring Data JPA** (Database)
   - **H2 Database** (In-memory DB cho dev)
   - **Spring Boot DevTools** (Hot reload)
   - **Validation** (Input validation)
4. Generate → Download → Extract → Open trong IntelliJ

### 2.2 Cấu trúc project mới

```
banking-api/
├── src/main/java/com/banking/
│   └── BankingApiApplication.java     # Main class
├── src/main/resources/
│   ├── application.properties         # Configuration
│   └── static/                        # Static files
├── src/test/java/com/banking/
│   └── BankingApiApplicationTests.java
└── pom.xml
```

### 2.3 Main class

```java
@SpringBootApplication   // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class BankingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingApiApplication.class, args);
    }
}
```

### 2.4 application.properties / application.yml

```yaml
# application.yml (khuyên dùng - dễ đọc hơn .properties)
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:bankingdb      # H2 in-memory database
    username: sa
    password:
  h2:
    console:
      enabled: true                  # Truy cập H2 console tại /h2-console
  jpa:
    hibernate:
      ddl-auto: create-drop         # Tự tạo table từ Entity
    show-sql: true                   # Log SQL queries
```

---

## 3. REST API BASICS

### 3.1 HTTP Methods

| Method | Ý nghĩa | CRUD | Ví dụ |
|--------|---------|------|-------|
| `GET` | Lấy dữ liệu | Read | `GET /api/accounts` |
| `POST` | Tạo mới | Create | `POST /api/accounts` |
| `PUT` | Cập nhật toàn bộ | Update | `PUT /api/accounts/1` |
| `PATCH` | Cập nhật 1 phần | Update | `PATCH /api/accounts/1` |
| `DELETE` | Xóa | Delete | `DELETE /api/accounts/1` |

### 3.2 HTTP Status Codes

| Code | Ý nghĩa | Khi nào dùng |
|------|---------|-------------|
| `200 OK` | Thành công | GET, PUT thành công |
| `201 Created` | Tạo mới thành công | POST thành công |
| `204 No Content` | Thành công, không có body | DELETE thành công |
| `400 Bad Request` | Request sai format | Validation fail |
| `401 Unauthorized` | Chưa đăng nhập | Missing/invalid token |
| `403 Forbidden` | Không có quyền | User không đủ role |
| `404 Not Found` | Không tìm thấy | ID không tồn tại |
| `409 Conflict` | Xung đột | Duplicate data |
| `500 Internal Server Error` | Lỗi server | Exception chưa handle |

### 3.3 RESTful URL Design

```
✅ TỐT (Danh từ, số nhiều):
GET    /api/accounts              # Lấy tất cả accounts
GET    /api/accounts/1            # Lấy account có id=1
POST   /api/accounts              # Tạo account mới
PUT    /api/accounts/1            # Cập nhật account id=1
DELETE /api/accounts/1            # Xóa account id=1
GET    /api/accounts/1/transactions  # Giao dịch của account 1

❌ XẤU:
GET    /api/getAccounts           # Đừng dùng động từ
GET    /api/account               # Dùng số nhiều
POST   /api/createAccount         # POST đã ngụ ý "create"
GET    /api/accounts/delete/1     # Dùng DELETE method
```

---

## 4. SPRING MVC ANNOTATIONS

### 4.1 Controller Annotations

```java
@RestController                    // Đánh dấu class là REST controller
@RequestMapping("/api/products")   // Base URL cho tất cả endpoints trong class
public class ProductController {

    @GetMapping                    // GET /api/products
    public List<Product> getAll() { }

    @GetMapping("/{id}")           // GET /api/products/1
    public Product getById(@PathVariable Long id) { }

    @GetMapping("/search")         // GET /api/products/search?name=iphone&minPrice=1000
    public List<Product> search(
        @RequestParam String name,
        @RequestParam(required = false, defaultValue = "0") double minPrice
    ) { }

    @PostMapping                   // POST /api/products
    public Product create(@RequestBody Product product) { }

    @PutMapping("/{id}")           // PUT /api/products/1
    public Product update(@PathVariable Long id, @RequestBody Product product) { }

    @DeleteMapping("/{id}")        // DELETE /api/products/1
    public void delete(@PathVariable Long id) { }
}
```

### 4.2 Annotation chi tiết

```java
// @PathVariable: Lấy giá trị từ URL path
@GetMapping("/accounts/{accountNumber}/transactions/{txId}")
public Transaction getTransaction(
    @PathVariable String accountNumber,  // Từ URL
    @PathVariable Long txId              // Từ URL
) { }
// GET /api/accounts/VCB001/transactions/42

// @RequestParam: Lấy giá trị từ query string
@GetMapping("/products")
public List<Product> search(
    @RequestParam(required = false) String category,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "price") String sortBy
) { }
// GET /api/products?category=electronics&page=0&size=20&sortBy=name

// @RequestBody: Lấy dữ liệu từ request body (JSON)
@PostMapping("/transfers")
public TransferResponse transfer(@RequestBody TransferRequest request) { }
// POST /api/transfers
// Body: { "fromAccount": "001", "toAccount": "002", "amount": 500000 }

// @RequestHeader: Lấy giá trị từ header
@GetMapping("/profile")
public Customer getProfile(@RequestHeader("Authorization") String token) { }
```

---

## 5. XÂY DỰNG CRUD API

### 5.1 Model class

```java
public class Product {
    private Long id;
    private String name;
    private double price;
    private String category;
    private int quantity;
    private boolean active;

    // Constructor
    public Product() {}

    public Product(Long id, String name, double price, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.active = true;
    }

    // Getters & Setters (tất cả fields)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
```

### 5.2 Controller đầy đủ

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Tạm dùng ArrayList (Ngày 7 sẽ dùng Database)
    private final List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    // GET /api/products - Lấy tất cả
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(products);
    }

    // GET /api/products/1 - Lấy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return products.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)                               // 200 OK
            .orElse(ResponseEntity.notFound().build());            // 404
    }

    // POST /api/products - Tạo mới
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        product.setId(nextId++);
        products.add(product);
        return ResponseEntity
            .status(HttpStatus.CREATED)    // 201 Created
            .body(product);
    }

    // PUT /api/products/1 - Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product updated) {

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                updated.setId(id);
                products.set(i, updated);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/products/1 - Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();    // 204
        }
        return ResponseEntity.notFound().build();         // 404
    }

    // GET /api/products/search?name=iphone&minPrice=1000000
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "999999999") double maxPrice) {

        List<Product> results = products.stream()
            .filter(p -> name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .toList();

        return ResponseEntity.ok(results);
    }
}
```

### 5.3 ResponseEntity

```java
// ResponseEntity cho phép control đầy đủ HTTP response

// 200 OK with body
return ResponseEntity.ok(product);

// 201 Created with body
return ResponseEntity.status(HttpStatus.CREATED).body(product);

// 204 No Content
return ResponseEntity.noContent().build();

// 400 Bad Request
return ResponseEntity.badRequest().body("Invalid request");

// 404 Not Found
return ResponseEntity.notFound().build();

// Custom headers
return ResponseEntity.ok()
    .header("X-Total-Count", String.valueOf(total))
    .body(products);
```

---

## 6. INPUT VALIDATION

### 6.1 Bean Validation annotations

```java
import jakarta.validation.constraints.*;

public class CreateProductRequest {

    @NotBlank(message = "Tên sản phẩm không được trống")
    @Size(min = 2, max = 100, message = "Tên từ 2-100 ký tự")
    private String name;

    @NotNull(message = "Giá không được null")
    @Min(value = 0, message = "Giá phải >= 0")
    @Max(value = 999999999, message = "Giá không vượt quá 999,999,999")
    private Double price;

    @NotBlank(message = "Category không được trống")
    private String category;

    @Min(value = 0, message = "Số lượng phải >= 0")
    private int quantity;

    @Email(message = "Email không hợp lệ")
    private String contactEmail;

    // Getters & Setters
}
```

### 6.2 Sử dụng @Valid trong Controller

```java
@PostMapping
public ResponseEntity<Product> createProduct(
        @Valid @RequestBody CreateProductRequest request) {  // @Valid kích hoạt validation
    // Nếu validation fail → tự động trả 400 Bad Request
    Product product = new Product();
    product.setName(request.getName());
    product.setPrice(request.getPrice());
    // ...
    return ResponseEntity.status(HttpStatus.CREATED).body(product);
}
```

---

## 7. EXCEPTION HANDLING

### 7.1 Custom Exception

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
    }
}

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountNumber) {
        super(String.format("Insufficient funds in account: %s", accountNumber));
    }
}
```

### 7.2 Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .toList();

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed: " + String.join(", ", errors),
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal server error",    // KHÔNG expose chi tiết lỗi cho client
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

// Error response DTO
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
    // Getters
}
```

---

## 8. TEST API VỚI POSTMAN

### 8.1 Cài đặt
- Download Postman: https://www.postman.com/downloads/
- Hoặc dùng Thunder Client (extension VS Code)

### 8.2 Tạo Collection

```
Banking API Collection
├── Products
│   ├── GET    /api/products              (Get All)
│   ├── GET    /api/products/1            (Get By ID)
│   ├── POST   /api/products              (Create)
│   ├── PUT    /api/products/1            (Update)
│   ├── DELETE /api/products/1            (Delete)
│   └── GET    /api/products/search?name=iphone  (Search)
└── Accounts
    ├── GET    /api/accounts
    ├── POST   /api/accounts
    └── ...
```

### 8.3 Ví dụ request

```
POST http://localhost:8080/api/products
Content-Type: application/json

{
    "name": "iPhone 15 Pro",
    "price": 28990000,
    "category": "Electronics",
    "quantity": 50
}

→ Response 201 Created:
{
    "id": 1,
    "name": "iPhone 15 Pro",
    "price": 28990000,
    "category": "Electronics",
    "quantity": 50,
    "active": true
}
```

---

## 9. TÓM TẮT NGÀY 6

```
┌──────────────────────────────────────────────────┐
│               NGÀY 6 - TÓM TẮT                  │
├──────────────────────────────────────────────────┤
│ Spring Boot:                                     │
│ ✓ Convention over Configuration                  │
│ ✓ Spring Initializr → tạo project nhanh         │
│ ✓ @SpringBootApplication = entry point           │
│                                                  │
│ REST API:                                        │
│ ✓ HTTP Methods: GET POST PUT DELETE              │
│ ✓ Status Codes: 200, 201, 204, 400, 404, 500    │
│ ✓ RESTful URL design (danh từ, số nhiều)        │
│                                                  │
│ Annotations:                                     │
│ ✓ @RestController, @RequestMapping               │
│ ✓ @GetMapping, @PostMapping, @PutMapping...      │
│ ✓ @PathVariable, @RequestParam, @RequestBody     │
│ ✓ @Valid + Bean Validation constraints           │
│                                                  │
│ Error Handling:                                   │
│ ✓ Custom exceptions                              │
│ ✓ @RestControllerAdvice + @ExceptionHandler      │
│ ✓ Consistent error response format               │
│                                                  │
│ ⚠️ KHÔNG expose stack trace cho client           │
│ ⚠️ Luôn validate input với @Valid                │
│ ⚠️ Dùng ResponseEntity cho full control          │
└──────────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. Spring Boot khác Spring Framework ở điểm nào?
2. `@RestController` khác `@Controller` gì?
3. `@PathVariable` và `@RequestParam` khác nhau thế nào?
4. Tại sao cần `@Valid`? Nếu không có thì sao?
5. HTTP 401 và 403 khác nhau gì?
6. Tại sao nên dùng `ResponseEntity` thay vì return trực tiếp?
