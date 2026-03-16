# GIÁO TRÌNH JAVA & TYPESCRIPT - BANKING & ECOMMERCE
## Từ Zero đến Junior Developer trong 15 ngày

> **Đối tượng:** Người mới, có kiến thức nền cơ bản về lập trình
> **Mục tiêu:** Tư duy độc lập, xây dựng được real project Banking & Ecommerce
> **Phương pháp:** Learning by Doing - 30% lý thuyết, 70% thực hành

---

## TỔNG QUAN GIÁO TRÌNH

```
┌─────────────────────────────────────────────────────────────────┐
│                    15-DAY LEARNING PATH                         │
├─────────────┬───────────────────┬───────────────────────────────┤
│ PHASE 1     │ PHASE 2           │ PHASE 3                       │
│ Ngày 1-5    │ Ngày 6-10         │ Ngày 11-15                    │
│ NỀN TẢNG   │ BACKEND + API     │ REAL PROJECT                  │
│             │                   │                               │
│ Java Core   │ Spring Boot       │ Mini Banking App              │
│ TypeScript  │ REST API          │ E-Commerce Platform           │
│ Git & Tools │ Database          │ Deploy & Present              │
└─────────────┴───────────────────┴───────────────────────────────┘
```

### Lịch học mỗi ngày (8 tiếng)

| Khung giờ | Hoạt động | Thời lượng |
|-----------|-----------|------------|
| 08:00 - 09:00 | Ôn bài hôm trước + Quiz nhanh | 1h |
| 09:00 - 11:00 | Lý thuyết mới + Demo | 2h |
| 11:00 - 12:00 | Bài tập có hướng dẫn (Guided Exercise) | 1h |
| 13:00 - 15:00 | Thực hành tự do (Self Practice) | 2h |
| 15:00 - 16:30 | Mini Project của ngày | 1.5h |
| 16:30 - 17:00 | Review code + Q&A | 0.5h |

---

## PHASE 1: NỀN TẢNG (Ngày 1-5)

---

### NGÀY 1: Java Fundamentals & Tư duy lập trình

**Mục tiêu:** Hiểu cách Java hoạt động, viết được chương trình đầu tiên

#### Lý thuyết (2h)
- JDK, JRE, JVM là gì? Tại sao Java phổ biến trong Banking?
- Cài đặt môi trường: JDK 17+, IntelliJ IDEA
- Cấu trúc 1 chương trình Java: `class`, `main method`
- Biến, kiểu dữ liệu: `int`, `double`, `String`, `boolean`
- Operators: `+`, `-`, `*`, `/`, `%`, `==`, `!=`, `&&`, `||`
- Input/Output: `Scanner`, `System.out.println`

#### Bài tập có hướng dẫn (1h)
```java
// Bài 1: Máy tính lãi suất đơn giản
// Input: số tiền gửi, lãi suất/năm, số tháng
// Output: tiền lãi, tổng tiền nhận được
public class SimpleInterestCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số tiền gửi (VND): ");
        double principal = scanner.nextDouble();
        // ... học viên tự hoàn thành
    }
}
```

#### Thực hành tự do (2h)
1. Viết chương trình đổi tiền VND ↔ USD
2. Tính BMI và đưa ra đánh giá
3. Kiểm tra số chẵn/lẻ, số nguyên tố

#### Mini Project: **"ATM Console đơn giản"**
- Hiển thị menu: Xem số dư, Nạp tiền, Rút tiền, Thoát
- Xử lý input từ người dùng
- Kiểm tra điều kiện rút tiền (không rút quá số dư)

**Checklist cuối ngày:**
- [ ] Hiểu JDK vs JRE vs JVM
- [ ] Viết được chương trình với input/output
- [ ] Sử dụng if/else thành thạo
- [ ] Hoàn thành ATM Console

---

### NGÀY 2: Control Flow, Arrays & Collections

**Mục tiêu:** Thành thạo vòng lặp, mảng, và các collection cơ bản

#### Lý thuyết (2h)
- `if/else`, `switch/case` nâng cao
- Vòng lặp: `for`, `while`, `do-while`, `for-each`
- Arrays: khai báo, truy cập, duyệt
- `ArrayList`, `HashMap` - khi nào dùng cái nào?
- `String` methods: `length()`, `charAt()`, `substring()`, `split()`, `equals()`

#### Bài tập có hướng dẫn (1h)
```java
// Quản lý danh sách giao dịch
ArrayList<String> transactions = new ArrayList<>();
HashMap<String, Double> accountBalances = new HashMap<>();

// Thêm giao dịch, tìm kiếm, thống kê
```

#### Thực hành tự do (2h)
1. Tìm giá trị lớn nhất/nhỏ nhất trong mảng giá sản phẩm
2. Đếm tần suất xuất hiện từng loại giao dịch
3. Sắp xếp danh sách sản phẩm theo giá

#### Mini Project: **"Quản lý giỏ hàng (Shopping Cart)"**
- Thêm/xóa sản phẩm (tên, giá, số lượng)
- Hiển thị giỏ hàng dạng bảng
- Tính tổng tiền, áp dụng giảm giá theo tier (>500K giảm 5%, >1M giảm 10%)

**Checklist cuối ngày:**
- [ ] Sử dụng thành thạo for, while
- [ ] Phân biệt ArrayList vs Array vs HashMap
- [ ] Xử lý String cơ bản
- [ ] Hoàn thành Shopping Cart

---

### NGÀY 3: OOP - Object Oriented Programming

**Mục tiêu:** Hiểu và áp dụng 4 trụ cột OOP vào bài toán thực tế

#### Lý thuyết (2h)
- **Class & Object:** Blueprint vs Instance
- **Encapsulation:** private fields, getter/setter, tại sao cần bảo vệ data?
- **Inheritance:** `extends`, `super`, code reuse
- **Polymorphism:** method overriding, `@Override`
- **Abstraction:** `abstract class`, `interface`
- Constructor, `this` keyword

#### Bài tập có hướng dẫn (1h)
```java
// Hệ thống tài khoản ngân hàng
public abstract class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;

    public abstract double calculateInterest();

    public void deposit(double amount) { /* ... */ }
    public boolean withdraw(double amount) { /* ... */ }
}

public class SavingsAccount extends BankAccount {
    private double interestRate;

    @Override
    public double calculateInterest() {
        return getBalance() * interestRate / 12;
    }
}

public class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    @Override
    public boolean withdraw(double amount) {
        // Cho phép rút quá số dư trong hạn mức overdraft
    }
}
```

#### Thực hành tự do (2h)
1. Tạo class `Product` với các thuộc tính và method
2. Tạo hierarchy: `Payment` → `CashPayment`, `CardPayment`, `EWalletPayment`
3. Implement `interface Discountable` cho các sản phẩm có thể giảm giá

#### Mini Project: **"Banking Account System"**
- Tạo được nhiều loại tài khoản (Savings, Checking, Fixed Deposit)
- Chuyển tiền giữa các tài khoản
- Lịch sử giao dịch cho từng tài khoản
- In statement dạng bảng

**Checklist cuối ngày:**
- [ ] Giải thích được 4 trụ cột OOP bằng ví dụ
- [ ] Biết khi nào dùng abstract class vs interface
- [ ] Tạo được class hierarchy có ý nghĩa
- [ ] Hoàn thành Banking Account System

---

### NGÀY 4: TypeScript Fundamentals & Modern JS

**Mục tiêu:** Chuyển đổi tư duy từ Java sang TypeScript, hiểu type system

#### Lý thuyết (2h)
- Tại sao TypeScript? So sánh với Java
- Cài đặt: Node.js, npm, TypeScript compiler
- Types: `string`, `number`, `boolean`, `array`, `tuple`, `enum`
- `interface` vs `type` - khi nào dùng gì?
- Functions: arrow functions, optional params, default values
- `async/await`, `Promise` - xử lý bất đồng bộ
- Destructuring, Spread operator, Template literals

#### Bài tập có hướng dẫn (1h)
```typescript
// So sánh Java vs TypeScript
// Java: class Product { private String name; ... }
// TypeScript:
interface Product {
  id: number;
  name: string;
  price: number;
  category: 'electronics' | 'clothing' | 'food'; // Union type
  inStock: boolean;
}

// Generic function - tương tự Java Generic
function filterItems<T>(items: T[], predicate: (item: T) => boolean): T[] {
  return items.filter(predicate);
}

const expensiveProducts = filterItems(products, p => p.price > 1000000);
```

#### Thực hành tự do (2h)
1. Convert ATM Console từ Java sang TypeScript
2. Tạo type-safe Shopping Cart với TypeScript
3. Xử lý async: đọc file, setTimeout simulation

#### Mini Project: **"Product Catalog CLI"**
- CRUD sản phẩm với TypeScript
- Tìm kiếm, lọc theo category/price range
- Export danh sách ra file JSON
- Type-safe toàn bộ

**Checklist cuối ngày:**
- [ ] Hiểu type system của TypeScript
- [ ] Phân biệt interface vs type
- [ ] Sử dụng async/await
- [ ] Hoàn thành Product Catalog CLI

---

### NGÀY 5: Git, Tools & Project Structure

**Mục tiêu:** Làm việc chuyên nghiệp với Git, hiểu cấu trúc dự án thực tế

#### Lý thuyết (2h)
- Git workflow: `init`, `add`, `commit`, `push`, `pull`
- Branching: `branch`, `checkout`, `merge`
- Giải quyết conflict
- `.gitignore` - những gì KHÔNG được commit (credentials, node_modules)
- Cấu trúc project Java (Maven/Gradle)
- Cấu trúc project TypeScript (package.json, tsconfig.json)
- **Security 101 trong Banking:** Không hardcode password, không log sensitive data

#### Bài tập có hướng dẫn (1h)
```
# Thực hành Git workflow
git init my-banking-project
cd my-banking-project
git checkout -b feature/add-account
# ... code ...
git add .
git commit -m "feat: add BankAccount class with deposit/withdraw"
git checkout main
git merge feature/add-account
```

```
# Cấu trúc project Java thực tế
banking-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/banking/
│   │   │       ├── model/          # Entity classes
│   │   │       ├── service/        # Business logic
│   │   │       ├── repository/     # Data access
│   │   │       ├── controller/     # API endpoints
│   │   │       └── dto/            # Data Transfer Objects
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
└── README.md
```

#### Thực hành tự do (2h)
1. Tạo repo, thực hành branching strategy
2. Tạo project structure cho cả Java và TypeScript
3. Setup Maven project với dependencies cơ bản
4. Setup TypeScript project với eslint, prettier

#### Mini Project: **"Tổ chức lại toàn bộ code từ ngày 1-4"**
- Đưa tất cả bài tập lên Git repo với commit messages chuẩn
- Tạo README.md mô tả project
- Tổ chức folder structure chuyên nghiệp
- Tạo branch riêng cho từng feature

**Checklist cuối ngày:**
- [ ] Sử dụng Git thành thạo (add, commit, branch, merge)
- [ ] Hiểu cấu trúc project chuẩn
- [ ] Biết security basics
- [ ] Repo gọn gàng, có README

---

## PHASE 2: BACKEND & API (Ngày 6-10)

---

### NGÀY 6: Spring Boot - Khởi động Backend

**Mục tiêu:** Tạo được REST API đầu tiên với Spring Boot

#### Lý thuyết (2h)
- Spring Boot là gì? Tại sao Banking dùng Spring?
- Spring Initializr: tạo project
- Annotations: `@RestController`, `@GetMapping`, `@PostMapping`, `@RequestBody`
- Request/Response lifecycle
- JSON serialization/deserialization
- Postman/Thunder Client để test API

#### Bài tập có hướng dẫn (1h)
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> products = new ArrayList<>();

    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        products.add(product);
        return product;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return products.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
```

#### Thực hành tự do (2h)
1. Tạo API CRUD cho `BankAccount`
2. Thêm validation: `@NotNull`, `@Min`, `@Max`
3. Xử lý exceptions với `@ExceptionHandler`

#### Mini Project: **"E-Commerce Product API"**
- CRUD Products với category
- Search products by name, filter by price range
- Pagination: `?page=0&size=10`
- Proper error responses (HTTP status codes)

**Checklist cuối ngày:**
- [ ] Tạo được Spring Boot project
- [ ] Viết CRUD REST API
- [ ] Test API bằng Postman
- [ ] Hiểu HTTP methods & status codes

---

### NGÀY 7: Database & JPA

**Mục tiêu:** Kết nối database, lưu trữ data persistent

#### Lý thuyết (2h)
- SQL cơ bản: `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `JOIN`
- H2 Database (in-memory) → PostgreSQL
- JPA/Hibernate: ORM là gì?
- Annotations: `@Entity`, `@Id`, `@GeneratedValue`, `@Column`
- `JpaRepository` - CRUD tự động
- Relationships: `@OneToMany`, `@ManyToOne`, `@ManyToMany`
- Database migration với Flyway

#### Bài tập có hướng dẫn (1h)
```java
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String ownerName;

    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByOwnerNameContaining(String name);

    @Query("SELECT a FROM Account a WHERE a.balance > :minBalance")
    List<Account> findWealthyAccounts(@Param("minBalance") BigDecimal minBalance);
}
```

#### Thực hành tự do (2h)
1. Tạo Entity cho Product, Category với relationship
2. Viết custom queries
3. Seed data với `data.sql` hoặc `CommandLineRunner`

#### Mini Project: **"Banking Transaction System"**
- Entity: Account, Transaction, Customer
- Gửi tiền, rút tiền, chuyển khoản → lưu DB
- Xem lịch sử giao dịch theo account
- Thống kê: tổng giao dịch trong ngày/tháng

**Checklist cuối ngày:**
- [ ] Viết SQL cơ bản
- [ ] Tạo Entity với JPA annotations
- [ ] Sử dụng JpaRepository
- [ ] Data được lưu persistent trong DB

---

### NGÀY 8: Service Layer, DTO & Business Logic

**Mục tiêu:** Tách biệt layers, viết business logic cho Banking & Ecommerce

#### Lý thuyết (2h)
- Layered Architecture: Controller → Service → Repository
- DTO (Data Transfer Object) pattern - tại sao không trả Entity trực tiếp?
- MapStruct hoặc manual mapping
- `@Service`, `@Transactional`
- Business rules trong Banking:
  - Validate số dư trước khi rút
  - Giới hạn giao dịch/ngày
  - Tính phí chuyển khoản
- Business rules trong Ecommerce:
  - Kiểm tra tồn kho trước khi đặt hàng
  - Tính giá sau khuyến mãi
  - Áp dụng voucher

#### Bài tập có hướng dẫn (1h)
```java
// Service layer với business logic
@Service
@Transactional
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferResponse transfer(TransferRequest request) {
        Account sender = accountRepository
            .findByAccountNumber(request.getFromAccount())
            .orElseThrow(() -> new AccountNotFoundException(request.getFromAccount()));

        Account receiver = accountRepository
            .findByAccountNumber(request.getToAccount())
            .orElseThrow(() -> new AccountNotFoundException(request.getToAccount()));

        // Business rule: kiểm tra số dư
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(sender.getAccountNumber());
        }

        // Business rule: giới hạn chuyển khoản 500M/ngày
        BigDecimal dailyTotal = transactionRepository
            .sumTodayTransactions(sender.getId());
        if (dailyTotal.add(request.getAmount())
            .compareTo(new BigDecimal("500000000")) > 0) {
            throw new DailyLimitExceededException();
        }

        // Thực hiện chuyển khoản
        sender.debit(request.getAmount());
        receiver.credit(request.getAmount());

        // Lưu giao dịch
        Transaction transaction = Transaction.builder()
            .fromAccount(sender)
            .toAccount(receiver)
            .amount(request.getAmount())
            .type(TransactionType.TRANSFER)
            .timestamp(LocalDateTime.now())
            .build();
        transactionRepository.save(transaction);

        return new TransferResponse("SUCCESS", transaction.getId());
    }
}
```

#### Thực hành tự do (2h)
1. Tạo OrderService cho Ecommerce với business rules
2. Implement DTO pattern cho tất cả API responses
3. Xử lý edge cases: concurrent transfers, invalid data

#### Mini Project: **"Nâng cấp Banking & Ecommerce API"**
- Refactor code từ ngày 6-7 theo layered architecture
- Thêm đầy đủ business rules
- Tất cả API trả về DTO (không leak entity)
- Global exception handler

**Checklist cuối ngày:**
- [ ] Hiểu layered architecture
- [ ] Implement DTO pattern
- [ ] Viết business logic có validation
- [ ] Code clean, tách biệt rõ ràng

---

### NGÀY 9: Authentication & Security

**Mục tiêu:** Bảo mật API với JWT, hiểu security trong Banking

#### Lý thuyết (2h)
- Authentication vs Authorization
- Spring Security basics
- JWT (JSON Web Token): cấu trúc, cách hoạt động
- Password hashing: BCrypt
- Role-based access control (RBAC): `ADMIN`, `USER`, `TELLER`
- CORS configuration
- **Banking security concepts:**
  - Tại sao dùng `BigDecimal` không dùng `double` cho tiền?
  - Audit logging - ghi lại mọi thao tác
  - Rate limiting

#### Bài tập có hướng dẫn (1h)
```java
// JWT Authentication Flow
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        // Hash password, save user
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Verify credentials, generate JWT
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

// Protected endpoint
@RestController
@RequestMapping("/api/accounts")
@PreAuthorize("hasRole('USER')")
public class AccountController {

    @GetMapping("/my-accounts")
    public List<AccountDTO> getMyAccounts(@AuthenticationPrincipal User user) {
        return accountService.getAccountsByUser(user.getId());
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public TransferResponse transfer(
        @AuthenticationPrincipal User user,
        @Valid @RequestBody TransferRequest request) {
        return transferService.transfer(user.getId(), request);
    }
}
```

#### Thực hành tự do (2h)
1. Implement đầy đủ JWT auth flow
2. Tạo roles: ADMIN (xem tất cả), USER (xem của mình)
3. Thêm audit logging cho mọi giao dịch

#### Mini Project: **"Secured Banking API"**
- Register/Login với JWT
- User chỉ xem/thao tác account của mình
- Admin xem tất cả accounts và transactions
- Audit log cho mọi action

**Checklist cuối ngày:**
- [ ] Hiểu JWT flow
- [ ] Implement authentication & authorization
- [ ] Password được hash, không lưu plain text
- [ ] API được bảo vệ đúng cách

---

### NGÀY 10: TypeScript Backend - NestJS & Integration

**Mục tiêu:** Xây dựng API tương tự bằng TypeScript, hiểu sự khác biệt

#### Lý thuyết (2h)
- NestJS framework - "Angular cho Backend"
- So sánh Spring Boot vs NestJS
- Decorators: `@Controller`, `@Get`, `@Post`, `@Injectable`
- TypeORM - tương tự JPA
- Validation với `class-validator`
- Middleware, Guards, Pipes

#### Bài tập có hướng dẫn (1h)
```typescript
// NestJS Controller - so sánh với Spring Boot
@Controller('api/products')
export class ProductController {
  constructor(private readonly productService: ProductService) {}

  @Get()
  async findAll(@Query() query: PaginationDto): Promise<Product[]> {
    return this.productService.findAll(query);
  }

  @Post()
  @UseGuards(JwtAuthGuard)
  async create(@Body() createProductDto: CreateProductDto): Promise<Product> {
    return this.productService.create(createProductDto);
  }
}

// TypeORM Entity - so sánh với JPA
@Entity()
export class Product {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @Column('decimal', { precision: 15, scale: 2 })
  price: number;

  @ManyToOne(() => Category, category => category.products)
  category: Category;
}
```

#### Thực hành tự do (2h)
1. Tạo NestJS project với CRUD API
2. Kết nối PostgreSQL với TypeORM
3. Implement JWT auth tương tự Spring Boot

#### Mini Project: **"E-Commerce API với NestJS"**
- CRUD: Products, Categories, Orders
- JWT Authentication
- Validation đầy đủ
- So sánh code với Spring Boot version

**Checklist cuối ngày:**
- [ ] Tạo được NestJS project
- [ ] Hiểu sự tương đồng Spring Boot ↔ NestJS
- [ ] Kết nối database với TypeORM
- [ ] Hoàn thành E-Commerce API

---

## PHASE 3: REAL PROJECT (Ngày 11-15)

---

### NGÀY 11: Project Setup & Architecture Design

**Mục tiêu:** Thiết kế kiến trúc và setup project cho 2 ứng dụng thực tế

#### Buổi sáng: Thiết kế hệ thống (4h)

**Project A: Mini Banking Application (Java/Spring Boot)**
```
Tính năng:
├── User Management
│   ├── Register/Login (JWT)
│   └── Profile management
├── Account Management
│   ├── Tạo tài khoản (Savings, Checking)
│   ├── Xem số dư
│   └── Xem statement
├── Transactions
│   ├── Nạp tiền (Deposit)
│   ├── Rút tiền (Withdraw)
│   ├── Chuyển khoản (Transfer)
│   └── Lịch sử giao dịch
└── Admin Dashboard
    ├── Xem tất cả users/accounts
    ├── Thống kê giao dịch
    └── Freeze/Unfreeze account
```

**Project B: E-Commerce Platform (TypeScript/NestJS)**
```
Tính năng:
├── User Management
│   ├── Register/Login (JWT)
│   └── Address book
├── Product Catalog
│   ├── CRUD Products (Admin)
│   ├── Categories
│   ├── Search & Filter
│   └── Pagination
├── Shopping Cart
│   ├── Add/Remove items
│   ├── Update quantity
│   └── Apply voucher
├── Order Management
│   ├── Place order (từ cart)
│   ├── Order status tracking
│   ├── Order history
│   └── Cancel order
└── Payment (Simulated)
    ├── Cash on Delivery
    ├── Bank Transfer
    └── E-Wallet
```

#### Buổi chiều: Setup & Database Design (4h)
- Vẽ ERD (Entity Relationship Diagram)
- Tạo project từ scratch
- Setup database, migrations
- Tạo tất cả entities/models
- Seed data cơ bản

**Checklist cuối ngày:**
- [ ] ERD hoàn chỉnh cho cả 2 project
- [ ] Project structure đã setup
- [ ] Tất cả entities đã tạo
- [ ] Database migration chạy thành công

---

### NGÀY 12: Core Features Implementation

**Mục tiêu:** Implement các tính năng cốt lõi

#### Banking App (Java - 4h)
- [ ] User registration & login với JWT
- [ ] Tạo account (auto-generate account number)
- [ ] Deposit & Withdraw với validation
- [ ] Transfer giữa 2 accounts
- [ ] Transaction history với pagination

#### E-Commerce App (TypeScript - 4h)
- [ ] User registration & login với JWT
- [ ] Product CRUD (Admin only)
- [ ] Category management
- [ ] Product search & filter
- [ ] Shopping cart (add, remove, update quantity)

**Code quality rules cho ngày này:**
```
1. Mỗi method không quá 20 dòng
2. Mỗi class có single responsibility
3. Tất cả API trả về consistent response format:
   {
     "success": true/false,
     "data": { ... },
     "message": "...",
     "timestamp": "2024-01-15T10:30:00"
   }
4. Validation ở cả controller (input) và service (business rule)
5. Meaningful error messages
```

**Checklist cuối ngày:**
- [ ] Authentication hoạt động
- [ ] Core CRUD features hoàn thành
- [ ] API responses consistent
- [ ] Test bằng Postman - tất cả pass

---

### NGÀY 13: Advanced Features & Business Logic

**Mục tiêu:** Thêm business logic phức tạp, xử lý edge cases

#### Banking App - Advanced (4h)
- [ ] Daily transfer limit (500M VND/ngày)
- [ ] Transaction fee calculation (chuyển khác ngân hàng: 0.05%, min 10K, max 50K)
- [ ] Account freeze/unfreeze (Admin)
- [ ] Monthly statement generation
- [ ] Scheduled interest calculation cho Savings Account
- [ ] Concurrent transfer handling (pessimistic locking)

```java
// Ví dụ: Transaction fee calculation
public class FeeCalculator {
    public BigDecimal calculateTransferFee(TransferType type, BigDecimal amount) {
        if (type == TransferType.INTERNAL) {
            return BigDecimal.ZERO; // Miễn phí nội bộ
        }

        BigDecimal fee = amount.multiply(new BigDecimal("0.0005")); // 0.05%
        BigDecimal minFee = new BigDecimal("10000");
        BigDecimal maxFee = new BigDecimal("50000");

        if (fee.compareTo(minFee) < 0) return minFee;
        if (fee.compareTo(maxFee) > 0) return maxFee;
        return fee;
    }
}
```

#### E-Commerce App - Advanced (4h)
- [ ] Voucher system (% discount, fixed amount, min order value)
- [ ] Inventory management (check stock before order)
- [ ] Order placement workflow (Cart → Order → Payment)
- [ ] Order status flow: PENDING → CONFIRMED → SHIPPING → DELIVERED / CANCELLED
- [ ] Order cancellation (chỉ khi PENDING hoặc CONFIRMED)
- [ ] Simple recommendation: "Sản phẩm tương tự"

```typescript
// Ví dụ: Voucher system
interface Voucher {
  code: string;
  type: 'PERCENTAGE' | 'FIXED_AMOUNT';
  value: number;
  minOrderValue: number;
  maxDiscount?: number;
  expiryDate: Date;
  usageLimit: number;
  usedCount: number;
}

function applyVoucher(cart: Cart, voucher: Voucher): ApplyResult {
  if (voucher.usedCount >= voucher.usageLimit) {
    throw new VoucherExpiredException('Voucher đã hết lượt sử dụng');
  }
  if (new Date() > voucher.expiryDate) {
    throw new VoucherExpiredException('Voucher đã hết hạn');
  }
  if (cart.totalAmount < voucher.minOrderValue) {
    throw new MinOrderNotMetException(voucher.minOrderValue);
  }
  // Calculate discount...
}
```

**Checklist cuối ngày:**
- [ ] Business logic phức tạp hoạt động đúng
- [ ] Edge cases được xử lý
- [ ] Concurrent operations an toàn
- [ ] Tất cả features test bằng Postman pass

---

### NGÀY 14: Testing, Documentation & Polish

**Mục tiêu:** Viết test, hoàn thiện documentation, fix bugs

#### Buổi sáng: Testing (4h)
- Unit test với JUnit 5 (Java) / Jest (TypeScript)
- Integration test cho API endpoints
- Test business logic edge cases

```java
// Unit test ví dụ
@SpringBootTest
class TransferServiceTest {

    @Test
    void shouldTransferSuccessfully() {
        // Given
        Account sender = createAccount("001", BigDecimal.valueOf(1000000));
        Account receiver = createAccount("002", BigDecimal.ZERO);

        TransferRequest request = new TransferRequest("001", "002",
            BigDecimal.valueOf(500000));

        // When
        TransferResponse response = transferService.transfer(request);

        // Then
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(sender.getBalance()).isEqualByComparingTo("500000");
        assertThat(receiver.getBalance()).isEqualByComparingTo("500000");
    }

    @Test
    void shouldThrowWhenInsufficientFunds() {
        Account sender = createAccount("001", BigDecimal.valueOf(100));
        TransferRequest request = new TransferRequest("001", "002",
            BigDecimal.valueOf(500000));

        assertThatThrownBy(() -> transferService.transfer(request))
            .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldThrowWhenDailyLimitExceeded() {
        // Test daily limit logic
    }
}
```

```typescript
// Jest test ví dụ
describe('OrderService', () => {
  it('should place order successfully when stock is available', async () => {
    const cart = await cartService.getCart(userId);
    const order = await orderService.placeOrder(userId, cart.id);

    expect(order.status).toBe('PENDING');
    expect(order.items).toHaveLength(cart.items.length);
  });

  it('should throw when product is out of stock', async () => {
    await expect(orderService.placeOrder(userId, cartId))
      .rejects.toThrow(OutOfStockException);
  });
});
```

#### Buổi chiều: Documentation & Polish (4h)
- API Documentation với Swagger/OpenAPI
- README.md cho cả 2 project
- Fix bugs phát hiện trong quá trình test
- Code cleanup, remove unused code
- Postman collection export

**Checklist cuối ngày:**
- [ ] Unit tests cho core business logic (>70% coverage cho service layer)
- [ ] Integration tests cho main API flows
- [ ] Swagger documentation
- [ ] README hoàn chỉnh
- [ ] Không còn known bugs

---

### NGÀY 15: Deployment, Presentation & Review

**Mục tiêu:** Deploy ứng dụng, trình bày project, nhận feedback

#### Buổi sáng: Deployment (3h)
- Dockerize ứng dụng
- Docker Compose cho app + database
- Deploy lên cloud (Railway / Render / Fly.io - free tier)

```dockerfile
# Dockerfile cho Spring Boot
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'
services:
  banking-api:
    build: ./banking-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/banking
    depends_on:
      - db

  ecommerce-api:
    build: ./ecommerce-app
    ports:
      - "3000:3000"
    environment:
      - DATABASE_URL=postgresql://postgres:password@db:5432/ecommerce
    depends_on:
      - db

  db:
    image: postgres:15-alpine
    environment:
      POSTGRES_PASSWORD: password
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

#### Buổi chiều: Presentation & Review (5h)

**Trình bày project (2h):**
1. Demo live cả 2 ứng dụng
2. Giải thích architecture decisions
3. Show code quality: clean code, test coverage
4. Nói về challenges gặp phải và cách giải quyết

**Code Review & Feedback (2h):**
- Review code với mentor/peers
- Identify areas for improvement
- Discussion: "Nếu có thêm 1 tuần, bạn sẽ làm gì?"

**Tổng kết & Roadmap (1h):**
- Đánh giá tiến bộ
- Kiến thức cần đào sâu thêm
- Lộ trình tiếp theo (xem phần dưới)

**Checklist cuối ngày:**
- [ ] Ứng dụng chạy trên Docker
- [ ] Deploy thành công lên cloud
- [ ] Demo presentation hoàn chỉnh
- [ ] Nhận và ghi chú feedback

---

## ĐÁNH GIÁ & TIÊU CHÍ HOÀN THÀNH

### Rubric đánh giá

| Tiêu chí | Đạt (Pass) | Tốt (Good) | Xuất sắc (Excellent) |
|-----------|------------|-------------|----------------------|
| **Code Quality** | Code chạy được, có structure | Clean code, tách layer rõ | SOLID principles, design patterns |
| **Functionality** | CRUD cơ bản hoạt động | Business logic phức tạp | Edge cases xử lý tốt |
| **Security** | Có authentication | JWT + RBAC | Audit logging, rate limiting |
| **Testing** | Không có test | Unit tests cơ bản | Unit + Integration > 70% |
| **Git** | Commit không rõ ràng | Conventional commits | Branching strategy + PR |
| **Deployment** | Chạy local | Dockerized | Deployed on cloud |

### Điểm tối thiểu để Pass: Hoàn thành 70% checklist mỗi ngày

---

## LỘ TRÌNH TIẾP THEO (Sau 15 ngày)

```
Tuần 3-4: Frontend
├── React (TypeScript) cho E-Commerce
├── Kết nối với Backend API
└── Responsive design

Tuần 5-6: Advanced Backend
├── Microservices basics
├── Message Queue (RabbitMQ/Kafka)
├── Caching (Redis)
└── Performance optimization

Tuần 7-8: DevOps & Production
├── CI/CD pipeline (GitHub Actions)
├── Monitoring & Logging (ELK Stack)
├── Cloud deployment (AWS/GCP)
└── Security hardening

Tháng 3+: Specialization
├── Domain-Driven Design (DDD)
├── Event Sourcing (Banking)
├── Payment Gateway Integration
└── System Design & Architecture
```

---

## TÀI NGUYÊN HỌC TẬP

### Tài liệu chính
- [Java Official Docs](https://docs.oracle.com/en/java/)
- [Spring Boot Reference](https://spring.io/projects/spring-boot)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [NestJS Documentation](https://docs.nestjs.com/)

### Video courses (Miễn phí)
- Java Programming - freeCodeCamp (YouTube)
- Spring Boot Tutorial - Amigoscode (YouTube)
- TypeScript Full Course - Net Ninja (YouTube)
- NestJS Crash Course - Traversy Media (YouTube)

### Practice
- [LeetCode](https://leetcode.com/) - Luyện thuật toán
- [HackerRank](https://www.hackerrank.com/) - Java challenges
- [Exercism](https://exercism.org/) - TypeScript track

### Books
- "Clean Code" - Robert C. Martin
- "Head First Java" - Kathy Sierra
- "Effective TypeScript" - Dan Vanderkam

---

## PHỤ LỤC: CÁC QUY TẮC CODING TRONG BANKING

### 1. Luôn dùng BigDecimal cho tiền
```java
// SAI - mất độ chính xác
double balance = 100.10;
double fee = 0.05;
double result = balance - fee; // 100.04999999999998

// ĐÚNG
BigDecimal balance = new BigDecimal("100.10");
BigDecimal fee = new BigDecimal("0.05");
BigDecimal result = balance.subtract(fee); // 100.05
```

### 2. Không bao giờ log sensitive data
```java
// SAI
log.info("User login: username={}, password={}", username, password);
log.info("Transfer: from={}, amount={}", accountNumber, amount);

// ĐÚNG
log.info("User login: username={}", username);
log.info("Transfer: from=****{}, amount=***",
    accountNumber.substring(accountNumber.length() - 4));
```

### 3. Validate ở mọi layer
```java
// Controller: validate input format
@PostMapping("/transfer")
public Response transfer(@Valid @RequestBody TransferRequest request) { }

// Service: validate business rules
public void transfer(TransferRequest request) {
    validateSufficientFunds(request);
    validateDailyLimit(request);
    validateAccountStatus(request);
    // then execute...
}

// Repository: database constraints
@Column(nullable = false)
@Check(constraints = "balance >= 0")
private BigDecimal balance;
```

### 4. Audit trail cho mọi thao tác
```java
@Entity
public class AuditLog {
    private Long id;
    private String userId;
    private String action;        // "TRANSFER", "LOGIN", "ACCOUNT_CREATE"
    private String details;       // JSON chi tiết
    private String ipAddress;
    private LocalDateTime timestamp;
    private String status;        // "SUCCESS", "FAILED"
}
```

---

> **Lưu ý quan trọng:** Giáo trình này thiết kế cho 8h/ngày học tập tập trung.
> Nếu học part-time (4h/ngày), nhân đôi thời gian → 30 ngày.
> Điều quan trọng nhất: **THỰC HÀNH, THỰC HÀNH, và THỰC HÀNH.**
> Đọc code, viết code, debug code - đó là cách duy nhất để thành developer.
