# BANKING CODING RULES - Cheat Sheet

## 1. LUÔN dùng BigDecimal cho tiền

```java
// SAI - floating point error
double a = 0.1 + 0.2; // = 0.30000000000000004

// ĐÚNG
BigDecimal a = new BigDecimal("0.1").add(new BigDecimal("0.2")); // = 0.3
```

**Quy tắc:**
- `new BigDecimal("100.50")` ✅ (dùng String)
- `new BigDecimal(100.50)` ❌ (dùng double → mất chính xác)
- So sánh: `a.compareTo(b) > 0` thay vì `a > b`

## 2. KHÔNG BAO GIỜ log sensitive data

```java
// ❌ SAI
log.info("Password: {}", password);
log.info("Card: {}", cardNumber);
log.info("Account: {}", fullAccountNumber);

// ✅ ĐÚNG
log.info("Login attempt for user: {}", username);
log.info("Card: ****{}", cardNumber.substring(cardNumber.length() - 4));
```

## 3. Validate ở MỌI layer

| Layer | Validate gì? | Ví dụ |
|-------|--------------|-------|
| Controller | Input format | `@NotNull`, `@Min(0)`, `@Email` |
| Service | Business rules | Số dư đủ?, Giới hạn/ngày?, Account active? |
| Repository | DB constraints | `UNIQUE`, `NOT NULL`, `CHECK (balance >= 0)` |

## 4. Transaction phải ATOMIC

```java
@Transactional // Nếu 1 bước fail → rollback tất cả
public void transfer(String from, String to, BigDecimal amount) {
    debit(from, amount);   // Bước 1
    credit(to, amount);    // Bước 2
    logTransaction(...);   // Bước 3
    // Nếu bước 3 fail → bước 1 & 2 cũng rollback
}
```

## 5. Audit Trail

Mọi thao tác PHẢI được ghi log:
- WHO: userId
- WHAT: action (TRANSFER, LOGIN, CREATE_ACCOUNT...)
- WHEN: timestamp
- WHERE: IP address
- RESULT: SUCCESS / FAILED + reason

## 6. Idempotency

Cùng 1 request gửi 2 lần → kết quả giống nhau, tiền chỉ trừ 1 lần.

```java
// Dùng idempotency key
@PostMapping("/transfer")
public Response transfer(
    @RequestHeader("X-Idempotency-Key") String key,
    @RequestBody TransferRequest request) {
    // Check if key already processed
    if (transactionRepo.existsByIdempotencyKey(key)) {
        return transactionRepo.findByIdempotencyKey(key).getResponse();
    }
    // Process normally...
}
```

## 7. Rate Limiting

- Login: max 5 attempts / 15 phút
- Transfer: max 10 transactions / giờ
- API: max 100 requests / phút

## 8. Password Rules

```java
// Hash password TRƯỚC KHI lưu DB
String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));

// Verify
boolean matches = BCrypt.checkpw(rawPassword, hashed);
```

KHÔNG BAO GIỜ:
- Lưu plain text password
- Dùng MD5/SHA1 (quá yếu)
- Tự implement crypto
