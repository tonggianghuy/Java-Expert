# NGÀY 7: Database & JPA

---

## 1. DATABASE FUNDAMENTALS

### 1.1 Tại sao cần Database?

```
Ngày 6: Dữ liệu lưu trong ArrayList
         → Restart server = MẤT HẾT data!

Ngày 7: Dữ liệu lưu trong Database
         → Restart server = data VẪN CÒN (persistent)
```

### 1.2 Relational Database (RDBMS)

Dữ liệu được tổ chức thành **bảng (tables)** với **hàng (rows)** và **cột (columns)**.

```
Table: accounts
┌────┬──────────────┬───────────────┬──────────────┬──────────┐
│ id │ account_no   │ owner_name    │ balance      │ type     │
├────┼──────────────┼───────────────┼──────────────┼──────────┤
│ 1  │ VCB001       │ Nguyen Van A  │ 15,000,000   │ SAVINGS  │
│ 2  │ VCB002       │ Tran Thi B    │  8,500,000   │ CHECKING │
│ 3  │ VCB003       │ Le Van C      │ 42,000,000   │ SAVINGS  │
└────┴──────────────┴───────────────┴──────────────┴──────────┘

Table: transactions
┌────┬────────────┬──────────┬──────────┬─────────────┬─────────────────────┐
│ id │ account_id │ type     │ amount   │ balance_after│ created_at          │
├────┼────────────┼──────────┼──────────┼─────────────┼─────────────────────┤
│ 1  │ 1          │ DEPOSIT  │ 5000000  │ 20000000    │ 2024-01-15 10:30:00 │
│ 2  │ 1          │ WITHDRAW │ 2000000  │ 18000000    │ 2024-01-15 14:20:00 │
│ 3  │ 2          │ TRANSFER │ 1000000  │  7500000    │ 2024-01-15 15:00:00 │
└────┴────────────┴──────────┴──────────┴─────────────┴─────────────────────┘
```

### 1.3 Database lựa chọn

| Database | Khi nào dùng |
|----------|-------------|
| **H2** | Development, testing (in-memory) |
| **PostgreSQL** | Production - Banking, Ecommerce (**khuyên dùng**) |
| **MySQL** | Production - phổ biến, dễ dùng |
| **Oracle** | Enterprise Banking (đắt tiền) |

---

## 2. SQL CƠ BẢN

### 2.1 DDL - Data Definition Language

```sql
-- Tạo bảng
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,           -- Auto-increment ID
    account_number VARCHAR(20) UNIQUE NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    account_type VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Sửa bảng
ALTER TABLE accounts ADD COLUMN email VARCHAR(100);
ALTER TABLE accounts DROP COLUMN email;

-- Xóa bảng
DROP TABLE IF EXISTS transactions;
```

### 2.2 DML - Data Manipulation Language

```sql
-- INSERT: Thêm dữ liệu
INSERT INTO accounts (account_number, owner_name, balance, account_type)
VALUES ('VCB001', 'Nguyen Van A', 15000000.00, 'SAVINGS');

INSERT INTO accounts (account_number, owner_name, balance, account_type)
VALUES ('VCB002', 'Tran Thi B', 8500000.00, 'CHECKING');

-- SELECT: Lấy dữ liệu
SELECT * FROM accounts;                          -- Lấy tất cả
SELECT account_number, owner_name, balance       -- Chọn cột
FROM accounts
WHERE account_type = 'SAVINGS';                  -- Điều kiện

SELECT * FROM accounts
WHERE balance > 10000000                         -- Số dư > 10M
AND is_active = TRUE                             -- Đang active
ORDER BY balance DESC                            -- Sắp xếp giảm dần
LIMIT 10 OFFSET 0;                              -- Phân trang

-- UPDATE: Cập nhật
UPDATE accounts
SET balance = balance + 5000000
WHERE account_number = 'VCB001';

-- DELETE: Xóa
DELETE FROM transactions WHERE id = 1;
```

### 2.3 Aggregate Functions

```sql
-- Đếm
SELECT COUNT(*) FROM accounts;                    -- Tổng số tài khoản
SELECT COUNT(*) FROM accounts WHERE account_type = 'SAVINGS';

-- Tổng
SELECT SUM(balance) FROM accounts;                -- Tổng tất cả số dư
SELECT SUM(amount) FROM transactions
WHERE account_id = 1 AND transaction_type = 'DEPOSIT';

-- Trung bình, Min, Max
SELECT AVG(balance) FROM accounts;
SELECT MIN(balance), MAX(balance) FROM accounts;

-- GROUP BY
SELECT account_type, COUNT(*), AVG(balance)
FROM accounts
GROUP BY account_type;
-- SAVINGS  | 2 | 28500000
-- CHECKING | 1 |  8500000

-- HAVING (filter sau GROUP BY)
SELECT account_type, AVG(balance) as avg_balance
FROM accounts
GROUP BY account_type
HAVING AVG(balance) > 10000000;
```

### 2.4 JOIN

```sql
-- INNER JOIN: Lấy records khớp ở CẢ HAI bảng
SELECT a.account_number, a.owner_name, t.transaction_type, t.amount, t.created_at
FROM accounts a
INNER JOIN transactions t ON a.id = t.account_id
WHERE a.account_number = 'VCB001'
ORDER BY t.created_at DESC;

-- LEFT JOIN: Lấy TẤT CẢ từ bảng trái, records khớp từ bảng phải
SELECT a.account_number, a.owner_name, COUNT(t.id) as transaction_count
FROM accounts a
LEFT JOIN transactions t ON a.id = t.account_id
GROUP BY a.id, a.account_number, a.owner_name;
-- Hiển thị cả accounts KHÔNG có transaction nào (count = 0)
```

---

## 3. JPA & HIBERNATE

### 3.1 ORM là gì?

```
ORM = Object-Relational Mapping

Java Object          ←→         Database Table
─────────────                   ──────────────
class Account        ←→         accounts table
  Long id            ←→         id BIGSERIAL
  String accountNo   ←→         account_number VARCHAR
  Double balance     ←→         balance DECIMAL
  List<Transaction>  ←→         JOIN transactions

JPA  = Specification (interface/standard)
Hibernate = Implementation (thư viện thực thi JPA)
```

### 3.2 Entity class

```java
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity                              // Đánh dấu class là entity (= 1 table)
@Table(name = "accounts")           // Tên table trong DB
public class Account {

    @Id                              // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "owner_name", nullable = false, length = 100)
    private String ownerName;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)     // Lưu enum dạng String
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Auto-set timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors, Getters, Setters
    public Account() {}

    // ... getters & setters
}

public enum AccountType {
    SAVINGS, CHECKING, FIXED_DEPOSIT
}
```

### 3.3 Relationships

```java
// ONE-TO-MANY: 1 Account có nhiều Transactions
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ... other fields

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();
}

// MANY-TO-ONE: Nhiều Transactions thuộc 1 Account
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "balance_after", precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

// MANY-TO-MANY: Products ↔ Categories (Ecommerce)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();
}
```

**Fetch Types:**
| Type | Behavior | Khi nào dùng |
|------|---------|-------------|
| `LAZY` | Load khi truy cập | Mặc định cho `@OneToMany`, `@ManyToMany` |
| `EAGER` | Load ngay cùng parent | Mặc định cho `@ManyToOne`, `@OneToOne` |

> **Best practice:** Luôn dùng `LAZY` và fetch khi cần bằng JOIN FETCH query.

---

## 4. SPRING DATA JPA REPOSITORY

### 4.1 JpaRepository - CRUD tự động

```java
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Entity, ID type>
// KHÔNG cần viết implementation! Spring tự tạo.
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Đã có sẵn:
    // save(entity)           - Tạo hoặc cập nhật
    // findById(id)           - Tìm theo ID → Optional<Account>
    // findAll()              - Lấy tất cả
    // findAll(Pageable)      - Lấy có phân trang
    // deleteById(id)         - Xóa theo ID
    // count()                - Đếm
    // existsById(id)         - Kiểm tra tồn tại
}
```

### 4.2 Query Methods (Derived Queries)

Spring tự tạo SQL query từ tên method:

```java
public interface AccountRepository extends JpaRepository<Account, Long> {

    // findBy + FieldName
    Optional<Account> findByAccountNumber(String accountNumber);

    // findBy + Field + Condition
    List<Account> findByAccountType(AccountType type);
    List<Account> findByActiveTrue();
    List<Account> findByActiveFalse();

    // Multiple conditions
    List<Account> findByAccountTypeAndActiveTrue(AccountType type);

    // Like / Containing
    List<Account> findByOwnerNameContaining(String name);
    List<Account> findByOwnerNameContainingIgnoreCase(String name);

    // Comparison
    List<Account> findByBalanceGreaterThan(BigDecimal amount);
    List<Account> findByBalanceBetween(BigDecimal min, BigDecimal max);

    // Ordering
    List<Account> findByAccountTypeOrderByBalanceDesc(AccountType type);

    // Counting
    long countByAccountType(AccountType type);

    // Exists
    boolean existsByAccountNumber(String accountNumber);

    // Top/First
    List<Account> findTop10ByOrderByBalanceDesc();  // Top 10 giàu nhất

    // Pagination
    Page<Account> findByAccountType(AccountType type, Pageable pageable);
}
```

**Quy tắc đặt tên method:**

```
findBy     + FieldName  + Condition  + OrderBy + FieldName + Direction
deleteBy   + ...
countBy    + ...
existsBy   + ...

Conditions:
- Containing, StartingWith, EndingWith
- GreaterThan, LessThan, Between
- IsNull, IsNotNull
- In, NotIn
- True, False
- Before, After (Date)
- OrderBy...Asc/Desc
```

### 4.3 Custom Queries (@Query)

```java
public interface AccountRepository extends JpaRepository<Account, Long> {

    // JPQL (Java Persistence Query Language) - dùng tên Entity/field
    @Query("SELECT a FROM Account a WHERE a.balance > :minBalance AND a.active = true")
    List<Account> findWealthyActiveAccounts(@Param("minBalance") BigDecimal minBalance);

    // Native SQL - dùng tên table/column
    @Query(value = "SELECT * FROM accounts WHERE balance > :min ORDER BY balance DESC LIMIT :limit",
           nativeQuery = true)
    List<Account> findTopAccounts(@Param("min") BigDecimal min, @Param("limit") int limit);

    // Aggregate query
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.accountType = :type")
    BigDecimal sumBalanceByType(@Param("type") AccountType type);

    // Update query
    @Modifying
    @Query("UPDATE Account a SET a.active = false WHERE a.id = :id")
    void deactivateAccount(@Param("id") Long id);

    // JOIN query
    @Query("SELECT a FROM Account a JOIN FETCH a.transactions t " +
           "WHERE a.accountNumber = :accountNo ORDER BY t.createdAt DESC")
    Account findAccountWithTransactions(@Param("accountNo") String accountNo);
}
```

---

## 5. SỬ DỤNG TRONG SERVICE & CONTROLLER

### 5.1 Service sử dụng Repository

```java
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    // Constructor injection (khuyên dùng)
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Account", "accountNumber", accountNumber));
    }

    public Account createAccount(Account account) {
        // Kiểm tra trùng account number
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new DuplicateResourceException("Account number already exists");
        }
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updated) {
        Account existing = getAccountById(id);
        existing.setOwnerName(updated.getOwnerName());
        existing.setAccountType(updated.getAccountType());
        return accountRepository.save(existing);   // save với ID có sẵn = UPDATE
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account", "id", id);
        }
        accountRepository.deleteById(id);
    }

    // Pagination
    public Page<Account> getAccounts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return accountRepository.findAll(pageable);
    }
}
```

### 5.2 Controller sử dụng Service

```java
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<Page<Account>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(accountService.getAccounts(page, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody Account account) {
        Account created = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

---

## 6. DATABASE MIGRATION VỚI FLYWAY

### 6.1 Flyway là gì?

Flyway quản lý **phiên bản database schema** giống Git quản lý code.

```
V1__create_accounts.sql    → Tạo bảng accounts
V2__create_transactions.sql → Tạo bảng transactions
V3__add_email_to_accounts.sql → Thêm cột email
```

### 6.2 Setup

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

```yaml
# application.yml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
```

### 6.3 Migration files

```sql
-- src/main/resources/db/migration/V1__create_accounts.sql
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    account_type VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- V2__create_transactions.sql
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2),
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_account ON transactions(account_id);
CREATE INDEX idx_transactions_date ON transactions(created_at);
```

---

## 7. SEED DATA

```java
@Component
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public DataSeeder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) {
        if (accountRepository.count() == 0) {
            Account acc1 = new Account();
            acc1.setAccountNumber("VCB001");
            acc1.setOwnerName("Nguyen Van A");
            acc1.setBalance(new BigDecimal("15000000"));
            acc1.setAccountType(AccountType.SAVINGS);

            Account acc2 = new Account();
            acc2.setAccountNumber("VCB002");
            acc2.setOwnerName("Tran Thi B");
            acc2.setBalance(new BigDecimal("8500000"));
            acc2.setAccountType(AccountType.CHECKING);

            accountRepository.saveAll(List.of(acc1, acc2));
            System.out.println("Seeded 2 accounts");
        }
    }
}
```

---

## 8. TÓM TẮT NGÀY 7

```
┌──────────────────────────────────────────────────┐
│               NGÀY 7 - TÓM TẮT                  │
├──────────────────────────────────────────────────┤
│ SQL:                                             │
│ ✓ DDL: CREATE TABLE, ALTER, DROP                 │
│ ✓ DML: SELECT, INSERT, UPDATE, DELETE            │
│ ✓ JOIN, GROUP BY, Aggregate functions            │
│                                                  │
│ JPA/Hibernate:                                   │
│ ✓ @Entity, @Table, @Column, @Id                  │
│ ✓ @GeneratedValue (auto ID)                      │
│ ✓ @OneToMany, @ManyToOne, @ManyToMany            │
│ ✓ FetchType.LAZY vs EAGER                        │
│                                                  │
│ Spring Data JPA:                                 │
│ ✓ JpaRepository = CRUD tự động                  │
│ ✓ Derived queries (findByXxx)                    │
│ ✓ @Query (JPQL + Native SQL)                     │
│ ✓ Pagination (Page, Pageable)                    │
│                                                  │
│ Migration:                                       │
│ ✓ Flyway = version control cho DB schema         │
│                                                  │
│ ⚠️ BigDecimal cho tiền, KHÔNG double             │
│ ⚠️ FetchType.LAZY mặc định                      │
│ ⚠️ save(entity có ID) = UPDATE                  │
└──────────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. ORM là gì? Tại sao dùng JPA thay vì viết SQL trực tiếp?
2. `@OneToMany` và `@ManyToOne` khác nhau thế nào?
3. LAZY vs EAGER loading: ưu nhược điểm?
4. `findByOwnerNameContainingIgnoreCase` tương đương SQL gì?
5. Tại sao cần Flyway? Không dùng `ddl-auto: update` luôn được không?
6. `save()` khi nào INSERT khi nào UPDATE?
