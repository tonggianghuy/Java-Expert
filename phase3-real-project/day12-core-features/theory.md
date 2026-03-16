# NGÀY 12: Core Features Implementation

---

## 1. MỤC TIÊU

Implement tất cả tính năng CRUD cơ bản cho cả 2 project.
**Nguyên tắc:** Hoàn thành flow cơ bản trước, optimize sau.

---

## 2. BANKING APP - CORE FEATURES

### 2.1 Implementation Order (thứ tự ưu tiên)

```
1. ✅ Auth: Register + Login (đã setup ngày 11)
2. → Account: Create + Get my accounts + Get by ID
3. → Deposit: Nạp tiền vào account
4. → Withdraw: Rút tiền từ account
5. → Transfer: Chuyển khoản giữa 2 accounts
6. → Transaction History: Xem lịch sử
```

### 2.2 Code Quality Rules

```java
// 1. Consistent Response Format
{
    "success": true/false,
    "data": { ... },
    "message": "...",
    "timestamp": "2024-01-15T10:30:00"
}

// 2. Validation ở Controller (format) + Service (business rule)

// 3. Mỗi method <= 20 dòng → tách thành helper methods

// 4. Meaningful error messages
"Số dư không đủ. Cần: 5,000,000, Hiện có: 3,000,000"  // ✅
"Error"                                                    // ❌
```

### 2.3 Account Service Implementation Guide

```java
@Service
public class AccountService {

    // CREATE ACCOUNT
    // 1. Verify user exists
    // 2. Generate unique account number (VCB + 7 digits)
    // 3. Set initial balance = 0 or from request
    // 4. Save to DB
    // 5. Return AccountResponse DTO

    // DEPOSIT
    // 1. Find account by ID
    // 2. Verify account belongs to current user
    // 3. Verify account is active
    // 4. Validate amount > 0
    // 5. Add to balance
    // 6. Create Transaction record
    // 7. Return updated balance

    // WITHDRAW
    // 1. Find account by ID
    // 2. Verify ownership + active
    // 3. Validate amount > 0
    // 4. Validate balance >= amount
    // 5. Subtract from balance
    // 6. Create Transaction record
    // 7. Return updated balance

    // TRANSFER
    // 1. Validate fromAccount != toAccount
    // 2. Find both accounts
    // 3. Verify sender ownership + active
    // 4. Verify receiver active
    // 5. Validate balance >= amount
    // 6. Debit sender, Credit receiver
    // 7. Create 2 Transaction records
    // 8. Return TransferResponse
}
```

---

## 3. ECOMMERCE APP - CORE FEATURES

### 3.1 Implementation Order

```
1. ✅ Auth: Register + Login (đã setup ngày 11)
2. → Product CRUD (Admin)
3. → Category CRUD (Admin)
4. → Product Search & Filter (Public)
5. → Cart: Add / Remove / Update quantity
6. → Cart: Get my cart with total
```

### 3.2 Product Service Guide

```typescript
@Injectable()
export class ProductService {

    // FIND ALL with search, filter, pagination
    // 1. Build query with QueryBuilder
    // 2. If keyword → WHERE name ILIKE '%keyword%'
    // 3. If categoryId → WHERE category_id = categoryId
    // 4. If minPrice/maxPrice → WHERE price BETWEEN min AND max
    // 5. Apply pagination (skip, take)
    // 6. Return { data, total, page, totalPages }

    // CREATE (Admin only)
    // 1. Validate category exists
    // 2. Create product entity
    // 3. Save and return

    // UPDATE (Admin only)
    // 1. Find product or throw 404
    // 2. Update only provided fields (Partial)
    // 3. Save and return
}
```

### 3.3 Cart Service Guide

```typescript
@Injectable()
export class CartService {

    // GET MY CART
    // 1. Find all CartItems for current user
    // 2. Include product info (JOIN)
    // 3. Calculate total
    // 4. Return { items, itemCount, totalAmount }

    // ADD ITEM
    // 1. Find product or throw 404
    // 2. Check stock (product.quantity >= requested quantity)
    // 3. Check if product already in cart
    //    - YES: update quantity
    //    - NO: create new CartItem
    // 4. Return updated cart

    // UPDATE QUANTITY
    // 1. Find CartItem or throw 404
    // 2. Verify ownership
    // 3. If quantity = 0 → remove item
    // 4. Check stock
    // 5. Update and return

    // REMOVE ITEM
    // 1. Find CartItem or throw 404
    // 2. Verify ownership
    // 3. Delete
}
```

---

## 4. TESTING CHECKLIST

Test mỗi feature bằng Postman ngay sau khi implement:

```
Banking:
□ POST /api/auth/register     → 201 Created
□ POST /api/auth/login        → 200 + token
□ POST /api/accounts          → 201 Created (with token)
□ GET  /api/accounts/my       → 200 + list accounts
□ POST /api/accounts/1/deposit  → 200 + new balance
□ POST /api/accounts/1/withdraw → 200 + new balance
□ POST /api/accounts/1/withdraw (quá số dư) → 400 error
□ POST /api/transfers         → 200 + transfer result
□ GET  /api/accounts/1/transactions → 200 + list

Ecommerce:
□ POST /api/auth/register     → 201 Created
□ POST /api/auth/login        → 200 + token
□ POST /api/admin/products    → 201 (admin token)
□ GET  /api/products          → 200 + list
□ GET  /api/products?name=iphone → 200 + filtered list
□ POST /api/cart/items        → 200 (user token)
□ GET  /api/cart              → 200 + cart with total
□ PUT  /api/cart/items/1      → 200 + updated
□ DELETE /api/cart/items/1    → 204
```

---

## 5. TIPS

1. **Implement từng endpoint, test ngay, commit ngay** - không đợi xong hết mới test
2. **Copy Postman requests vào Collection** - để dùng lại
3. **Khi gặp bug** - đọc error message cẩn thận, check logs
4. **Đừng perfectionist** - hoàn thành basic flow trước, polish sau (ngày 13-14)
