# NGÀY 11: Project Setup & Architecture Design

---

## 1. THIẾT KẾ HỆ THỐNG (System Design)

### 1.1 Quy trình thiết kế

```
1. Requirements Analysis    → Hiểu yêu cầu
2. Database Design (ERD)    → Thiết kế dữ liệu
3. API Design               → Định nghĩa endpoints
4. Architecture Design      → Tổ chức code
5. Implementation           → Viết code
```

---

## 2. PROJECT A: MINI BANKING APPLICATION

### 2.1 Requirements

```
Actors:
- USER: khách hàng ngân hàng
- ADMIN: quản trị viên

User Stories:
1. USER đăng ký / đăng nhập
2. USER tạo tài khoản (Savings, Checking)
3. USER nạp tiền vào tài khoản
4. USER rút tiền từ tài khoản
5. USER chuyển khoản giữa 2 tài khoản
6. USER xem lịch sử giao dịch
7. USER xem statement tháng
8. ADMIN xem tất cả users & accounts
9. ADMIN freeze/unfreeze account
10. ADMIN xem thống kê giao dịch
```

### 2.2 ERD (Entity Relationship Diagram)

```
┌──────────────┐       ┌──────────────────┐       ┌──────────────────┐
│    users     │       │    accounts      │       │  transactions    │
├──────────────┤       ├──────────────────┤       ├──────────────────┤
│ id (PK)      │──┐    │ id (PK)          │──┐    │ id (PK)          │
│ username     │  │    │ account_number   │  │    │ account_id (FK)  │
│ password_hash│  └───→│ user_id (FK)     │  └───→│ type             │
│ email        │       │ owner_name       │       │ amount           │
│ role         │       │ balance          │       │ balance_after    │
│ active       │       │ account_type     │       │ description      │
│ created_at   │       │ is_active        │       │ related_account  │
└──────────────┘       │ created_at       │       │ fee              │
                       └──────────────────┘       │ created_at       │
                                                  └──────────────────┘
                       ┌──────────────────┐
                       │   audit_logs     │
                       ├──────────────────┤
                       │ id (PK)          │
                       │ user_id          │
                       │ action           │
                       │ resource_type    │
                       │ details          │
                       │ status           │
                       │ ip_address       │
                       │ created_at       │
                       └──────────────────┘
```

### 2.3 API Endpoints

```
Auth:
POST   /api/auth/register          Register
POST   /api/auth/login             Login

Accounts (USER):
POST   /api/accounts               Create account
GET    /api/accounts/my             Get my accounts
GET    /api/accounts/{id}           Get account detail
POST   /api/accounts/{id}/deposit   Deposit
POST   /api/accounts/{id}/withdraw  Withdraw

Transfers (USER):
POST   /api/transfers               Transfer money
GET    /api/transfers/history       Transfer history

Transactions (USER):
GET    /api/accounts/{id}/transactions    Transaction history
GET    /api/accounts/{id}/statement       Monthly statement

Admin:
GET    /api/admin/users             All users
GET    /api/admin/accounts          All accounts
PUT    /api/admin/accounts/{id}/freeze    Freeze account
PUT    /api/admin/accounts/{id}/unfreeze  Unfreeze account
GET    /api/admin/stats             Transaction statistics
```

---

## 3. PROJECT B: E-COMMERCE PLATFORM

### 3.1 Requirements

```
Actors:
- CUSTOMER: người mua hàng
- ADMIN: quản trị viên

User Stories:
1.  CUSTOMER đăng ký / đăng nhập
2.  CUSTOMER xem danh sách sản phẩm (search, filter, pagination)
3.  CUSTOMER xem chi tiết sản phẩm
4.  CUSTOMER thêm/xóa sản phẩm vào giỏ hàng
5.  CUSTOMER cập nhật số lượng trong giỏ
6.  CUSTOMER áp dụng voucher
7.  CUSTOMER đặt hàng từ giỏ hàng
8.  CUSTOMER xem lịch sử đơn hàng
9.  CUSTOMER hủy đơn hàng (khi PENDING/CONFIRMED)
10. ADMIN CRUD sản phẩm
11. ADMIN CRUD categories
12. ADMIN xem / cập nhật trạng thái đơn hàng
13. ADMIN tạo / quản lý voucher
```

### 3.2 ERD

```
┌───────────┐     ┌──────────────┐     ┌──────────────┐
│   users   │     │  categories  │     │   products   │
├───────────┤     ├──────────────┤     ├──────────────┤
│ id (PK)   │     │ id (PK)      │  ┌─→│ id (PK)      │
│ username  │     │ name         │  │  │ name         │
│ password  │     │ description  │  │  │ price        │
│ email     │     └──────┬───────┘  │  │ description  │
│ role      │            │          │  │ quantity     │
│ created_at│            └──────────┼──│ category_id  │
└─────┬─────┘                       │  │ image_url    │
      │                             │  │ active       │
      │    ┌──────────────┐         │  │ created_at   │
      │    │  cart_items   │         │  └──────────────┘
      │    ├──────────────┤         │
      │    │ id (PK)      │         │
      ├───→│ user_id (FK) │         │
      │    │ product_id(FK)│────────┘
      │    │ quantity      │
      │    └──────────────┘
      │
      │    ┌──────────────┐     ┌──────────────┐
      │    │   orders     │     │ order_items   │
      │    ├──────────────┤     ├──────────────┤
      └───→│ id (PK)      │──┐  │ id (PK)      │
           │ user_id (FK) │  └─→│ order_id (FK)│
           │ subtotal     │     │ product_id   │
           │ discount     │     │ quantity     │
           │ total_amount │     │ unit_price   │
           │ status       │     └──────────────┘
           │ voucher_code │
           │ created_at   │     ┌──────────────┐
           └──────────────┘     │   vouchers   │
                                ├──────────────┤
                                │ id (PK)      │
                                │ code         │
                                │ type         │
                                │ value        │
                                │ min_order    │
                                │ max_discount │
                                │ expiry_date  │
                                │ usage_limit  │
                                │ used_count   │
                                └──────────────┘
```

### 3.3 API Endpoints

```
Auth:
POST   /api/auth/register
POST   /api/auth/login

Products (Public):
GET    /api/products                    List (search, filter, pagination)
GET    /api/products/:id                Detail
GET    /api/categories                  List categories

Cart (Customer):
GET    /api/cart                         Get my cart
POST   /api/cart/items                   Add item
PUT    /api/cart/items/:id               Update quantity
DELETE /api/cart/items/:id               Remove item
DELETE /api/cart                          Clear cart

Orders (Customer):
POST   /api/orders                       Place order
GET    /api/orders                       My orders
GET    /api/orders/:id                   Order detail
PUT    /api/orders/:id/cancel            Cancel order

Vouchers (Customer):
POST   /api/vouchers/validate            Validate voucher

Admin:
POST   /api/admin/products               Create product
PUT    /api/admin/products/:id            Update product
DELETE /api/admin/products/:id            Delete product
GET    /api/admin/orders                  All orders
PUT    /api/admin/orders/:id/status       Update order status
POST   /api/admin/vouchers               Create voucher
```

---

## 4. SETUP CHECKLIST

### Banking App (Java/Spring Boot)

```
□ Spring Initializr: Web, JPA, PostgreSQL, Security, Validation
□ application.yml: database config, JWT secret
□ Entity classes: User, Account, Transaction, AuditLog
□ Repository interfaces
□ SecurityConfig + JwtService + JwtFilter
□ Flyway migrations
□ Seed data (2 users, 3 accounts)
□ Chạy được, login thành công
```

### Ecommerce App (TypeScript/NestJS)

```
□ nest new ecommerce-api
□ Install: TypeORM, pg, class-validator, @nestjs/jwt, passport, bcrypt
□ Modules: Auth, Product, Cart, Order, Voucher
□ Entity classes: User, Product, Category, CartItem, Order, OrderItem, Voucher
□ .env configuration
□ Seed data (categories, sample products)
□ Chạy được, register + login thành công
```

---

## 5. TÓM TẮT

Ngày 11 là ngày **thiết kế và setup**:
- Vẽ ERD, định nghĩa API endpoints
- Tạo project, config database, setup auth
- Tạo tất cả entities, seed data
- Đảm bảo cả 2 project chạy được và login thành công
- **CHƯA cần implement business logic phức tạp** → Ngày 12-13
