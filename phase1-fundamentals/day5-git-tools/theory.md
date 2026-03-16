# NGÀY 5: Git, Tools & Project Structure

---

## 1. GIT - HỆ THỐNG QUẢN LÝ PHIÊN BẢN

### 1.1 Git là gì? Tại sao cần Git?

**Không có Git:**
```
project_v1/
project_v2_final/
project_v2_final_REAL/
project_v3_backup_truoc_khi_sua/
project_CUOI_CUNG_THAT_SU/     ← Quen thuộc?
```

**Có Git:**
```
project/
├── .git/       ← Git lưu TOÀN BỘ lịch sử ở đây
└── src/        ← Chỉ có 1 folder duy nhất
```

Git giúp:
- **Lịch sử:** Xem ai sửa gì, khi nào, tại sao
- **Rollback:** Quay lại bất kỳ phiên bản nào
- **Branching:** Nhiều người làm song song không conflict
- **Collaboration:** Team work hiệu quả

### 1.2 Git cơ bản - 3 vùng

```
Working Directory    →    Staging Area    →    Repository (.git)
(thư mục làm việc)        (khu chờ)            (kho lưu trữ)

    Sửa file          git add            git commit
    ──────→            ──────→            ──────→

    ←──────            ←──────            ←──────
    git checkout       git reset          git log
```

### 1.3 Các lệnh Git cơ bản

```bash
# === KHỞI TẠO ===
git init                          # Tạo repo mới
git clone <url>                   # Clone repo từ remote

# === WORKFLOW HÀNG NGÀY ===
git status                        # Xem trạng thái files
git add <file>                    # Đưa file vào staging
git add .                         # Đưa TẤT CẢ files vào staging
git commit -m "message"           # Lưu snapshot vào repo
git push origin <branch>          # Đẩy lên remote (GitHub)
git pull origin <branch>          # Kéo về từ remote

# === XEM LỊCH SỬ ===
git log                           # Xem lịch sử commit
git log --oneline                 # Xem ngắn gọn
git log --oneline --graph         # Xem dạng đồ thị
git diff                          # Xem thay đổi chưa stage
git diff --staged                 # Xem thay đổi đã stage

# === UNDO ===
git checkout -- <file>            # Bỏ thay đổi chưa stage
git reset HEAD <file>             # Bỏ file khỏi staging
git revert <commit-hash>          # Tạo commit đảo ngược
```

### 1.4 Branching - Nhánh

```
main ────●────●────●────●────●────●────  (production)
              │                   ↑
              └──●──●──●──●──────┘       feature/add-transfer
                     │        ↑
                     └──●────┘           fix/transfer-bug
```

```bash
# Tạo branch mới
git branch feature/add-transfer
git checkout feature/add-transfer
# Hoặc gọn hơn:
git checkout -b feature/add-transfer

# Xem danh sách branch
git branch                  # Local
git branch -r               # Remote
git branch -a               # Tất cả

# Chuyển branch
git checkout main
git switch main             # Git 2.23+ (khuyên dùng)

# Merge branch
git checkout main
git merge feature/add-transfer

# Xóa branch (sau khi merge)
git branch -d feature/add-transfer
```

### 1.5 Giải quyết Conflict

Khi 2 người sửa cùng 1 dòng code → conflict:

```
<<<<<<< HEAD
    double fee = amount * 0.001;    // Code của bạn (current)
=======
    double fee = amount * 0.002;    // Code người khác (incoming)
>>>>>>> feature/update-fee
```

**Cách xử lý:**
1. Mở file có conflict
2. Chọn giữ code nào (hoặc kết hợp cả hai)
3. Xóa các markers `<<<<<<<`, `=======`, `>>>>>>>`
4. `git add <file>` → `git commit`

### 1.6 Commit Message Convention

```bash
# Format: <type>: <short description>
#
# Types:
# feat:     Tính năng mới
# fix:      Sửa bug
# refactor: Refactor code (không thay đổi behavior)
# docs:     Chỉ thay đổi documentation
# test:     Thêm/sửa test
# chore:    Thay đổi build, config, dependencies

# Ví dụ:
git commit -m "feat: add transfer between accounts"
git commit -m "fix: prevent negative balance on withdraw"
git commit -m "refactor: extract fee calculation to separate service"
git commit -m "docs: add API documentation for transfer endpoint"
git commit -m "test: add unit tests for TransferService"
```

### 1.7 .gitignore

File `.gitignore` liệt kê những gì Git KHÔNG track:

```gitignore
# === Java ===
*.class
*.jar
target/
.idea/
*.iml

# === TypeScript/Node ===
node_modules/
dist/
*.js.map

# === IDE ===
.vscode/
.idea/
*.swp

# === OS ===
.DS_Store
Thumbs.db

# === BẢO MẬT - QUAN TRỌNG! ===
.env
.env.local
*.pem
*.key
credentials.json
application-secret.yml
```

> **⚠️ BANKING RULE:** TUYỆT ĐỐI KHÔNG commit: passwords, API keys, certificates, database credentials. Nếu lỡ commit, phải rotate (đổi) ngay lập tức.

---

## 2. CẤU TRÚC PROJECT JAVA (Maven)

### 2.1 Maven là gì?

Maven = công cụ quản lý project Java:
- **Build:** Compile, test, package
- **Dependencies:** Tự động download thư viện
- **Structure:** Quy ước cấu trúc folder chuẩn

### 2.2 Cấu trúc Maven project

```
banking-app/
├── pom.xml                          # Project config + dependencies
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/banking/
│   │   │       ├── BankingApplication.java    # Main class
│   │   │       ├── model/                     # Entity/Domain classes
│   │   │       │   ├── Account.java
│   │   │       │   ├── Transaction.java
│   │   │       │   └── Customer.java
│   │   │       ├── repository/                # Data access layer
│   │   │       │   ├── AccountRepository.java
│   │   │       │   └── TransactionRepository.java
│   │   │       ├── service/                   # Business logic
│   │   │       │   ├── AccountService.java
│   │   │       │   └── TransferService.java
│   │   │       ├── controller/                # API endpoints
│   │   │       │   ├── AccountController.java
│   │   │       │   └── TransferController.java
│   │   │       ├── dto/                       # Data Transfer Objects
│   │   │       │   ├── TransferRequest.java
│   │   │       │   └── TransferResponse.java
│   │   │       ├── exception/                 # Custom exceptions
│   │   │       │   ├── InsufficientFundsException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       └── config/                    # Configuration
│   │   │           └── SecurityConfig.java
│   │   └── resources/
│   │       ├── application.yml                # App configuration
│   │       ├── application-dev.yml            # Dev environment
│   │       ├── application-prod.yml           # Production
│   │       └── db/migration/                  # Database migrations
│   │           ├── V1__create_accounts.sql
│   │           └── V2__create_transactions.sql
│   └── test/
│       └── java/
│           └── com/banking/
│               ├── service/
│               │   └── TransferServiceTest.java
│               └── controller/
│                   └── AccountControllerTest.java
├── .gitignore
└── README.md
```

### 2.3 pom.xml cơ bản

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <groupId>com.banking</groupId>
    <artifactId>banking-app</artifactId>
    <version>1.0.0</version>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot JPA (Database) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- PostgreSQL Driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### 2.4 Maven commands

```bash
mvn clean compile       # Compile code
mvn test                # Chạy tests
mvn package             # Build JAR/WAR
mvn spring-boot:run     # Chạy Spring Boot app
mvn clean install       # Build + install vào local repo
```

---

## 3. CẤU TRÚC PROJECT TYPESCRIPT (NestJS)

### 3.1 Cấu trúc NestJS project

```
ecommerce-app/
├── package.json                     # Dependencies + scripts
├── tsconfig.json                    # TypeScript config
├── nest-cli.json                    # NestJS CLI config
├── src/
│   ├── main.ts                      # Entry point
│   ├── app.module.ts                # Root module
│   ├── common/                      # Shared utilities
│   │   ├── dto/
│   │   │   └── pagination.dto.ts
│   │   ├── filters/
│   │   │   └── http-exception.filter.ts
│   │   ├── guards/
│   │   │   └── jwt-auth.guard.ts
│   │   └── pipes/
│   │       └── validation.pipe.ts
│   └── modules/
│       ├── auth/                    # Authentication module
│       │   ├── auth.module.ts
│       │   ├── auth.controller.ts
│       │   ├── auth.service.ts
│       │   ├── dto/
│       │   │   ├── login.dto.ts
│       │   │   └── register.dto.ts
│       │   └── strategies/
│       │       └── jwt.strategy.ts
│       ├── product/                 # Product module
│       │   ├── product.module.ts
│       │   ├── product.controller.ts
│       │   ├── product.service.ts
│       │   ├── product.entity.ts
│       │   └── dto/
│       │       ├── create-product.dto.ts
│       │       └── update-product.dto.ts
│       ├── cart/                    # Shopping cart module
│       │   ├── cart.module.ts
│       │   ├── cart.controller.ts
│       │   ├── cart.service.ts
│       │   └── cart.entity.ts
│       └── order/                   # Order module
│           ├── order.module.ts
│           ├── order.controller.ts
│           ├── order.service.ts
│           ├── order.entity.ts
│           └── dto/
│               └── create-order.dto.ts
├── test/
│   ├── app.e2e-spec.ts
│   └── jest-e2e.json
├── .env                             # Environment variables (KHÔNG commit!)
├── .env.example                     # Template cho .env (commit)
├── .gitignore
└── README.md
```

### 3.2 package.json cơ bản

```json
{
  "name": "ecommerce-app",
  "version": "1.0.0",
  "scripts": {
    "build": "nest build",
    "start": "nest start",
    "start:dev": "nest start --watch",
    "start:prod": "node dist/main",
    "test": "jest",
    "test:watch": "jest --watch",
    "test:cov": "jest --coverage",
    "lint": "eslint \"src/**/*.ts\"",
    "format": "prettier --write \"src/**/*.ts\""
  },
  "dependencies": {
    "@nestjs/common": "^10.0.0",
    "@nestjs/core": "^10.0.0",
    "@nestjs/platform-express": "^10.0.0",
    "@nestjs/typeorm": "^10.0.0",
    "typeorm": "^0.3.0",
    "pg": "^8.11.0",
    "class-validator": "^0.14.0",
    "class-transformer": "^0.5.0"
  },
  "devDependencies": {
    "@nestjs/cli": "^10.0.0",
    "@types/node": "^20.0.0",
    "typescript": "^5.0.0",
    "jest": "^29.0.0",
    "ts-jest": "^29.0.0",
    "eslint": "^8.0.0",
    "prettier": "^3.0.0"
  }
}
```

---

## 4. SECURITY BASICS

### 4.1 Những gì KHÔNG được commit

```
❌ TUYỆT ĐỐI KHÔNG:
├── Passwords / Secret keys
├── API keys (Stripe, SendGrid, AWS...)
├── Database credentials
├── JWT secret
├── SSL certificates (.pem, .key)
├── .env files
└── Private SSH keys

✅ THAY VÀO ĐÓ:
├── Dùng .env file (local, KHÔNG commit)
├── Tạo .env.example (template, CÓ commit)
├── Environment variables trên server
└── Secret manager (AWS Secrets Manager, Vault)
```

### 4.2 .env và .env.example

```bash
# .env (KHÔNG commit - trong .gitignore)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=banking_db
DB_USER=admin
DB_PASSWORD=SuperSecret123!
JWT_SECRET=my-ultra-secure-jwt-secret-key
STRIPE_API_KEY=sk_live_xxxxxxxxxxxxx

# .env.example (CÓ commit - template)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=banking_db
DB_USER=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
STRIPE_API_KEY=your_stripe_key
```

### 4.3 Nguyên tắc bảo mật cơ bản

```
1. KHÔNG hardcode credentials trong code
2. KHÔNG log sensitive data (password, card number, SSN)
3. LUÔN hash password (BCrypt, Argon2)
4. LUÔN validate input (SQL injection, XSS)
5. LUÔN dùng HTTPS
6. Principle of Least Privilege: chỉ cấp quyền TỐI THIỂU cần thiết
```

---

## 5. CODING STANDARDS & TOOLS

### 5.1 Java: Code Style

```java
// ✅ TỐT
public class TransferService {
    private final AccountRepository accountRepository;

    public TransferResponse transfer(TransferRequest request) {
        Account sender = findAccountOrThrow(request.getFromAccount());
        Account receiver = findAccountOrThrow(request.getToAccount());
        validateSufficientFunds(sender, request.getAmount());
        executeTransfer(sender, receiver, request.getAmount());
        return TransferResponse.success();
    }

    private Account findAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}

// ❌ XẤU
public class TransferService {
    public Object doTransfer(Object req) {
        // 100 dòng code không tách method
        // Tên biến: a, b, x, temp
        // Không có validation
        // Catch Exception chung chung
    }
}
```

### 5.2 TypeScript: ESLint & Prettier

```bash
# Cài đặt
npm install --save-dev eslint @typescript-eslint/parser @typescript-eslint/eslint-plugin
npm install --save-dev prettier eslint-config-prettier
```

```json
// .eslintrc.json
{
  "parser": "@typescript-eslint/parser",
  "plugins": ["@typescript-eslint"],
  "extends": [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "prettier"
  ],
  "rules": {
    "no-console": "warn",
    "@typescript-eslint/no-unused-vars": "error",
    "@typescript-eslint/explicit-function-return-type": "warn"
  }
}
```

```json
// .prettierrc
{
  "semi": true,
  "trailingComma": "all",
  "singleQuote": true,
  "printWidth": 100,
  "tabWidth": 2
}
```

---

## 6. BÀI TẬP THỰC HÀNH

### Bài 1: Git Workflow (Bắt buộc)
1. Tạo repo `banking-exercises`
2. Tạo branch `feature/day5-practice`
3. Commit ít nhất 3 lần với conventional commit messages
4. Merge vào main
5. Tạo `.gitignore` phù hợp

### Bài 2: Tổ chức lại code (Bắt buộc)
- Đưa tất cả bài tập từ ngày 1-4 vào Git repo
- Tổ chức folder structure chuyên nghiệp
- Mỗi ngày = 1 folder, mỗi bài = 1 file rõ ràng
- Tạo README.md

### Bài 3: Tạo project structure (Nâng cao)
- Tạo cấu trúc cho cả Banking (Maven) và Ecommerce (NestJS)
- Setup `.env.example` cho cả hai
- Tạo `.gitignore` phù hợp

---

## 7. TÓM TẮT NGÀY 5

```
┌─────────────────────────────────────────────────┐
│              NGÀY 5 - TÓM TẮT                  │
├─────────────────────────────────────────────────┤
│ Git:                                            │
│ ✓ init, add, commit, push, pull                 │
│ ✓ branch, checkout, merge                       │
│ ✓ Giải quyết conflict                          │
│ ✓ Conventional commit messages                  │
│ ✓ .gitignore cho security                       │
│                                                 │
│ Project Structure:                              │
│ ✓ Java Maven: model/service/repository/controller│
│ ✓ TypeScript NestJS: modules/common             │
│ ✓ pom.xml (Java) / package.json (TS)           │
│                                                 │
│ Security:                                       │
│ ✓ KHÔNG commit credentials                      │
│ ✓ .env + .env.example pattern                   │
│ ✓ Hash passwords, validate input                │
│                                                 │
│ ⚠️ Lỡ commit secret → rotate ngay              │
│ ⚠️ Mỗi commit = 1 thay đổi logic              │
│ ⚠️ Branch name: feature/, fix/, refactor/       │
└─────────────────────────────────────────────────┘
```
