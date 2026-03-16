# NGÀY 8: Service Layer, DTO & Business Logic

---

## 1. LAYERED ARCHITECTURE

### 1.1 Tại sao cần tách layers?

```
❌ KHÔNG tách layer:
Controller xử lý request + business logic + database access + validation
→ 1 class 500+ dòng, không thể test, không thể reuse

✅ CÓ tách layer:
Controller  → Nhận request, trả response        (mỏng)
Service     → Business logic, validation         (dày - logic ở đây)
Repository  → Database access                    (mỏng)
```

### 1.2 Luồng dữ liệu

```
Client Request (JSON)
       │
       ▼
┌──────────────┐
│  Controller   │  Nhận DTO → gọi Service → trả DTO
│  (thin layer) │  KHÔNG có business logic
└──────┬───────┘
       │ DTO → Entity (mapping)
       ▼
┌──────────────┐
│  Service      │  Business logic, validation, orchestration
│  (thick layer)│  @Transactional cho database operations
└──────┬───────┘
       │ Entity
       ▼
┌──────────────┐
│  Repository   │  CRUD operations, custom queries
│  (thin layer) │  JPA auto-generate
└──────┬───────┘
       │ SQL
       ▼
┌──────────────┐
│  Database     │
└──────────────┘
```

### 1.3 Quy tắc vàng

```
1. Controller KHÔNG gọi Repository trực tiếp
2. Service KHÔNG biết về HttpRequest/HttpResponse
3. Repository KHÔNG có business logic
4. Entity KHÔNG được trả trực tiếp cho Client → dùng DTO
```

---

## 2. DTO PATTERN (Data Transfer Object)

### 2.1 Tại sao KHÔNG trả Entity trực tiếp?

```java
// ❌ Trả Entity trực tiếp → NGUY HIỂM!
@GetMapping("/accounts/{id}")
public Account getAccount(@PathVariable Long id) {
    return accountRepository.findById(id).get();
}

// Response sẽ chứa:
// {
//   "id": 1,
//   "accountNumber": "VCB001",
//   "ownerName": "A",
//   "balance": 15000000,
//   "passwordHash": "$2a$10$xxxxx",     ← LỘ PASSWORD!
//   "ssn": "079123456789",               ← LỘ CMND!
//   "transactions": [...]                 ← LOAD TOÀN BỘ transactions!
// }
```

**Lý do cần DTO:**
1. **Bảo mật:** Không expose sensitive fields (password, SSN...)
2. **Performance:** Không load quan hệ không cần thiết
3. **Tách biệt:** Thay đổi DB schema không ảnh hưởng API
4. **Validation:** Request DTO có validation riêng
5. **Flexibility:** Response shape khác DB schema

### 2.2 Tạo DTOs

```java
// === Request DTOs (input) ===

public class CreateAccountRequest {
    @NotBlank(message = "Tên chủ tài khoản không được trống")
    private String ownerName;

    @NotNull(message = "Loại tài khoản không được null")
    private AccountType accountType;

    private BigDecimal initialDeposit = BigDecimal.ZERO;

    // Getters & Setters
}

public class TransferRequest {
    @NotBlank
    private String fromAccount;

    @NotBlank
    private String toAccount;

    @NotNull
    @DecimalMin(value = "1000", message = "Số tiền chuyển tối thiểu 1,000 VND")
    @DecimalMax(value = "500000000", message = "Số tiền chuyển tối đa 500,000,000 VND")
    private BigDecimal amount;

    private String description;

    // Getters & Setters
}

// === Response DTOs (output) ===

public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private String accountType;
    private boolean active;
    private LocalDateTime createdAt;
    // KHÔNG có: passwordHash, ssn, internal fields

    // Getters & Setters
}

public class TransferResponse {
    private String status;            // "SUCCESS" | "FAILED"
    private String transactionId;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal senderBalanceAfter;
    private String message;
    private LocalDateTime timestamp;

    // Getters & Setters
}

// === Consistent API Response ===

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private LocalDateTime timestamp;

    // Static factory methods
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = "OK";
        response.timestamp = LocalDateTime.now();
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.data = null;
        response.message = message;
        response.timestamp = LocalDateTime.now();
        return response;
    }
}
```

### 2.3 Mapping Entity ↔ DTO

```java
// Manual mapping (đơn giản, rõ ràng)
public class AccountMapper {

    public static AccountResponse toResponse(Account account) {
        AccountResponse dto = new AccountResponse();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setOwnerName(account.getOwnerName());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType().name());
        dto.setActive(account.isActive());
        dto.setCreatedAt(account.getCreatedAt());
        return dto;
    }

    public static Account toEntity(CreateAccountRequest request) {
        Account account = new Account();
        account.setOwnerName(request.getOwnerName());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialDeposit());
        account.setAccountNumber(generateAccountNumber()); // auto-generate
        return account;
    }

    public static List<AccountResponse> toResponseList(List<Account> accounts) {
        return accounts.stream()
            .map(AccountMapper::toResponse)
            .toList();
    }

    private static String generateAccountNumber() {
        return "VCB" + String.format("%07d", System.nanoTime() % 10000000);
    }
}
```

---

## 3. SERVICE LAYER - BUSINESS LOGIC

### 3.1 Service cơ bản

```java
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                         TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = AccountMapper.toEntity(request);
        Account saved = accountRepository.save(account);
        return AccountMapper.toResponse(saved);
    }

    public AccountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
        return AccountMapper.toResponse(account);
    }

    public List<AccountResponse> getAllAccounts() {
        return AccountMapper.toResponseList(accountRepository.findAll());
    }
}
```

### 3.2 Business Logic phức tạp - Banking Transfer

```java
@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional  // ← Đảm bảo ACID: nếu 1 bước fail → rollback tất cả
    public TransferResponse transfer(TransferRequest request) {

        // 1. Validate: không chuyển cho chính mình
        if (request.getFromAccount().equals(request.getToAccount())) {
            throw new InvalidTransferException("Không thể chuyển cho chính mình");
        }

        // 2. Tìm tài khoản gửi
        Account sender = accountRepository
            .findByAccountNumber(request.getFromAccount())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Account", "number", request.getFromAccount()));

        // 3. Tìm tài khoản nhận
        Account receiver = accountRepository
            .findByAccountNumber(request.getToAccount())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Account", "number", request.getToAccount()));

        // 4. Validate: tài khoản active
        if (!sender.isActive()) {
            throw new AccountInactiveException(sender.getAccountNumber());
        }
        if (!receiver.isActive()) {
            throw new AccountInactiveException(receiver.getAccountNumber());
        }

        // 5. Tính phí chuyển khoản
        BigDecimal fee = calculateTransferFee(request.getAmount());

        // 6. Validate: đủ số dư (bao gồm phí)
        BigDecimal totalDebit = request.getAmount().add(fee);
        if (sender.getBalance().compareTo(totalDebit) < 0) {
            throw new InsufficientFundsException(sender.getAccountNumber());
        }

        // 7. Validate: giới hạn chuyển khoản trong ngày
        validateDailyLimit(sender.getId(), request.getAmount());

        // 8. Thực hiện chuyển khoản
        sender.setBalance(sender.getBalance().subtract(totalDebit));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // 9. Ghi giao dịch cho cả 2 bên
        Transaction senderTx = createTransaction(
            sender, TransactionType.TRANSFER_OUT,
            request.getAmount().negate(), sender.getBalance(),
            "Chuyển khoản đến " + receiver.getAccountNumber());

        Transaction receiverTx = createTransaction(
            receiver, TransactionType.TRANSFER_IN,
            request.getAmount(), receiver.getBalance(),
            "Nhận từ " + sender.getAccountNumber());

        transactionRepository.saveAll(List.of(senderTx, receiverTx));

        // 10. Trả response
        TransferResponse response = new TransferResponse();
        response.setStatus("SUCCESS");
        response.setTransactionId(senderTx.getId().toString());
        response.setAmount(request.getAmount());
        response.setFee(fee);
        response.setSenderBalanceAfter(sender.getBalance());
        response.setMessage("Chuyển khoản thành công");
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    private BigDecimal calculateTransferFee(BigDecimal amount) {
        // Nội bộ: miễn phí
        // Liên ngân hàng: 0.05%, min 10K, max 50K
        BigDecimal fee = amount.multiply(new BigDecimal("0.0005"));
        BigDecimal minFee = new BigDecimal("10000");
        BigDecimal maxFee = new BigDecimal("50000");

        if (fee.compareTo(minFee) < 0) return minFee;
        if (fee.compareTo(maxFee) > 0) return maxFee;
        return fee;
    }

    private void validateDailyLimit(Long accountId, BigDecimal amount) {
        BigDecimal dailyTotal = transactionRepository
            .sumTodayTransfersByAccountId(accountId);
        if (dailyTotal == null) dailyTotal = BigDecimal.ZERO;

        BigDecimal limit = new BigDecimal("500000000"); // 500M/ngày
        if (dailyTotal.add(amount).compareTo(limit) > 0) {
            throw new DailyLimitExceededException(
                "Vượt quá hạn mức chuyển khoản 500,000,000 VND/ngày");
        }
    }

    private Transaction createTransaction(Account account, TransactionType type,
                                          BigDecimal amount, BigDecimal balanceAfter,
                                          String description) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setBalanceAfter(balanceAfter);
        tx.setDescription(description);
        return tx;
    }
}
```

### 3.3 Business Logic - Ecommerce Order

```java
@Service
public class OrderService {

    @Transactional
    public OrderResponse placeOrder(Long userId, Long cartId, String voucherCode) {

        // 1. Lấy giỏ hàng
        Cart cart = cartRepository.findByIdAndUserId(cartId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Giỏ hàng trống");
        }

        // 2. Kiểm tra tồn kho cho TỪNG sản phẩm
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new OutOfStockException(
                    String.format("'%s' chỉ còn %d sản phẩm",
                        product.getName(), product.getQuantity()));
            }
        }

        // 3. Tính tổng tiền
        BigDecimal subtotal = calculateSubtotal(cart);

        // 4. Áp dụng voucher (nếu có)
        BigDecimal discount = BigDecimal.ZERO;
        if (voucherCode != null) {
            discount = applyVoucher(voucherCode, subtotal);
        }

        BigDecimal totalAmount = subtotal.subtract(discount);

        // 5. Tạo đơn hàng
        Order order = new Order();
        order.setUserId(userId);
        order.setSubtotal(subtotal);
        order.setDiscount(discount);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);

        // 6. Chuyển cart items sang order items
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getProduct().getPrice());
            order.addItem(orderItem);

            // 7. Trừ tồn kho
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        Order savedOrder = orderRepository.save(order);

        // 8. Xóa giỏ hàng
        cartRepository.delete(cart);

        return OrderMapper.toResponse(savedOrder);
    }
}
```

---

## 4. @TRANSACTIONAL

### 4.1 ACID trong Banking

```
A - Atomicity:    Tất cả hoặc không gì cả (chuyển khoản: trừ + cộng phải cùng thành công)
C - Consistency:  Data luôn hợp lệ (balance >= 0)
I - Isolation:    Các giao dịch không ảnh hưởng lẫn nhau
D - Durability:   Đã commit = không mất
```

### 4.2 Sử dụng @Transactional

```java
@Service
public class TransferService {

    // Read-only: tối ưu performance cho queries
    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long id) {
        return AccountMapper.toResponse(accountRepository.findById(id).get());
    }

    // Write: mặc định, rollback khi có RuntimeException
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        // Nếu BẤT KỲ bước nào throw exception
        // → TẤT CẢ thay đổi DB bị rollback
    }

    // Chỉ định rollback cho checked exception
    @Transactional(rollbackFor = Exception.class)
    public void processPayment(PaymentRequest request) throws PaymentException {
        // ...
    }
}
```

### 4.3 Khi nào cần @Transactional?

```
✅ CẦN:
- Chuyển khoản (debit sender + credit receiver)
- Đặt hàng (tạo order + trừ tồn kho + xóa cart)
- Bất kỳ operation nào thay đổi NHIỀU records cùng lúc

❌ KHÔNG CẦN:
- Chỉ đọc 1 record
- Không thay đổi database
```

---

## 5. CONSISTENT RESPONSE FORMAT

### 5.1 Chuẩn hóa tất cả API responses

```java
// Mọi API đều trả về cùng format
{
    "success": true,
    "data": { ... },          // null nếu error
    "message": "OK",
    "timestamp": "2024-01-15T10:30:00"
}

// Error response
{
    "success": false,
    "data": null,
    "message": "Số dư không đủ để thực hiện giao dịch",
    "timestamp": "2024-01-15T10:30:00"
}
```

### 5.2 Controller sử dụng ApiResponse

```java
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAll() {
        List<AccountResponse> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(
            @Valid @RequestBody CreateAccountRequest request) {
        AccountResponse created = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(created));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(
            @Valid @RequestBody TransferRequest request) {
        TransferResponse result = transferService.transfer(request);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
```

---

## 6. TÓM TẮT NGÀY 8

```
┌──────────────────────────────────────────────────┐
│               NGÀY 8 - TÓM TẮT                  │
├──────────────────────────────────────────────────┤
│ Layered Architecture:                            │
│ ✓ Controller (thin) → Service (thick) → Repo    │
│ ✓ Mỗi layer có responsibility riêng             │
│                                                  │
│ DTO Pattern:                                     │
│ ✓ Request DTO: input validation                  │
│ ✓ Response DTO: hide sensitive fields            │
│ ✓ Entity ≠ DTO (tách biệt)                     │
│ ✓ Mapper: Entity ↔ DTO                          │
│                                                  │
│ Business Logic:                                  │
│ ✓ Validation ở Service layer                    │
│ ✓ Fee calculation, daily limits                  │
│ ✓ Stock checking, voucher logic                  │
│                                                  │
│ @Transactional:                                  │
│ ✓ Đảm bảo ACID cho multi-step operations       │
│ ✓ readOnly=true cho queries                      │
│ ✓ Auto rollback khi RuntimeException             │
│                                                  │
│ ⚠️ KHÔNG trả Entity trực tiếp ra API           │
│ ⚠️ KHÔNG có business logic trong Controller     │
│ ⚠️ BigDecimal.compareTo() thay vì >, <, ==     │
└──────────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. Tại sao cần tách Controller / Service / Repository?
2. Tại sao KHÔNG nên trả Entity trực tiếp cho client?
3. `@Transactional` hoạt động thế nào? Khi nào cần dùng?
4. Business rule "giới hạn chuyển khoản 500M/ngày" implement ở layer nào?
5. Request DTO và Response DTO khác nhau thế nào?
6. `readOnly = true` trong `@Transactional` có tác dụng gì?
