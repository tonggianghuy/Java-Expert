# NGÀY 13: Advanced Features & Business Logic

---

## 1. MỤC TIÊU

Thêm business logic phức tạp, xử lý edge cases, nâng cao chất lượng code.

---

## 2. BANKING APP - ADVANCED FEATURES

### 2.1 Transfer Fee Calculation

```java
public class FeeCalculator {

    // Nội bộ: miễn phí
    // Liên ngân hàng: 0.05%, min 10K, max 50K
    public static BigDecimal calculate(TransferType type, BigDecimal amount) {
        if (type == TransferType.INTERNAL) {
            return BigDecimal.ZERO;
        }

        BigDecimal feeRate = new BigDecimal("0.0005");
        BigDecimal fee = amount.multiply(feeRate);

        BigDecimal min = new BigDecimal("10000");
        BigDecimal max = new BigDecimal("50000");

        if (fee.compareTo(min) < 0) return min;
        if (fee.compareTo(max) > 0) return max;
        return fee.setScale(0, RoundingMode.CEILING);
    }
}
```

### 2.2 Daily Transfer Limit

```java
// Repository: Tính tổng chuyển khoản trong ngày
@Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
       "WHERE t.account.id = :accountId " +
       "AND t.type = 'TRANSFER_OUT' " +
       "AND t.createdAt >= :startOfDay")
BigDecimal sumTodayTransfers(@Param("accountId") Long accountId,
                             @Param("startOfDay") LocalDateTime startOfDay);

// Service: Validate
private void validateDailyLimit(Long accountId, BigDecimal amount) {
    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    BigDecimal todayTotal = transactionRepository
        .sumTodayTransfers(accountId, startOfDay);

    BigDecimal limit = new BigDecimal("500000000"); // 500M
    if (todayTotal.add(amount).compareTo(limit) > 0) {
        BigDecimal remaining = limit.subtract(todayTotal);
        throw new DailyLimitExceededException(
            String.format("Hạn mức còn lại hôm nay: %s VND",
                remaining.toPlainString()));
    }
}
```

### 2.3 Account Freeze / Unfreeze (Admin)

```java
@PreAuthorize("hasRole('ADMIN')")
public AccountResponse freezeAccount(Long accountId, String reason) {
    Account account = findById(accountId);
    account.setActive(false);
    accountRepository.save(account);

    auditService.log("FREEZE_ACCOUNT", "ACCOUNT",
        accountId.toString(), "Reason: " + reason, "SUCCESS");

    return AccountMapper.toResponse(account);
}
```

### 2.4 Monthly Statement

```java
public StatementResponse getMonthlyStatement(Long accountId, int year, int month) {
    Account account = findById(accountId);

    LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime end = start.plusMonths(1);

    List<Transaction> transactions = transactionRepository
        .findByAccountIdAndCreatedAtBetween(accountId, start, end);

    BigDecimal totalDeposit = transactions.stream()
        .filter(t -> t.getType() == TransactionType.DEPOSIT)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalWithdraw = transactions.stream()
        .filter(t -> t.getType() == TransactionType.WITHDRAW)
        .map(t -> t.getAmount().abs())
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return StatementResponse.builder()
        .accountNumber(account.getAccountNumber())
        .period(year + "-" + String.format("%02d", month))
        .openingBalance(/* balance đầu tháng */)
        .closingBalance(account.getBalance())
        .totalDeposits(totalDeposit)
        .totalWithdrawals(totalWithdraw)
        .transactionCount(transactions.size())
        .transactions(TransactionMapper.toResponseList(transactions))
        .build();
}
```

---

## 3. ECOMMERCE APP - ADVANCED FEATURES

### 3.1 Voucher System

```typescript
// Voucher entity
@Entity('vouchers')
export class Voucher {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ unique: true })
    code: string;

    @Column({ type: 'enum', enum: VoucherType })
    type: VoucherType;  // 'PERCENTAGE' | 'FIXED_AMOUNT'

    @Column('decimal')
    value: number;  // 10 (= 10%) hoặc 50000 (= 50K VND)

    @Column('decimal', { name: 'min_order_value' })
    minOrderValue: number;

    @Column('decimal', { name: 'max_discount', nullable: true })
    maxDiscount: number | null;  // Cap cho PERCENTAGE

    @Column({ name: 'expiry_date' })
    expiryDate: Date;

    @Column({ name: 'usage_limit' })
    usageLimit: number;

    @Column({ name: 'used_count', default: 0 })
    usedCount: number;
}

// Voucher validation & calculation
validateAndCalculateDiscount(voucher: Voucher, orderTotal: number): number {
    // 1. Check expiry
    if (new Date() > voucher.expiryDate) {
        throw new BadRequestException('Voucher đã hết hạn');
    }

    // 2. Check usage limit
    if (voucher.usedCount >= voucher.usageLimit) {
        throw new BadRequestException('Voucher đã hết lượt sử dụng');
    }

    // 3. Check min order value
    if (orderTotal < voucher.minOrderValue) {
        throw new BadRequestException(
            `Đơn hàng tối thiểu ${voucher.minOrderValue.toLocaleString()} VND`);
    }

    // 4. Calculate discount
    let discount: number;
    if (voucher.type === VoucherType.PERCENTAGE) {
        discount = orderTotal * voucher.value / 100;
        if (voucher.maxDiscount && discount > voucher.maxDiscount) {
            discount = voucher.maxDiscount;
        }
    } else {
        discount = voucher.value;
    }

    return Math.min(discount, orderTotal);  // Không giảm quá tổng đơn
}
```

### 3.2 Order Placement Workflow

```typescript
@Injectable()
export class OrderService {

    @Transaction()  // TypeORM transaction
    async placeOrder(userId: number, dto: PlaceOrderDto) {
        // 1. Get cart items
        const cartItems = await this.cartItemRepository.find({
            where: { userId },
            relations: ['product'],
        });

        if (cartItems.length === 0) {
            throw new BadRequestException('Giỏ hàng trống');
        }

        // 2. Validate stock for ALL items
        for (const item of cartItems) {
            if (item.product.quantity < item.quantity) {
                throw new BadRequestException(
                    `"${item.product.name}" chỉ còn ${item.product.quantity} sản phẩm`);
            }
        }

        // 3. Calculate subtotal
        const subtotal = cartItems.reduce(
            (sum, item) => sum + item.product.price * item.quantity, 0);

        // 4. Apply voucher if provided
        let discount = 0;
        if (dto.voucherCode) {
            const voucher = await this.voucherRepository.findOne({
                where: { code: dto.voucherCode },
            });
            if (!voucher) throw new NotFoundException('Voucher không tồn tại');
            discount = this.validateAndCalculateDiscount(voucher, subtotal);
            voucher.usedCount++;
            await this.voucherRepository.save(voucher);
        }

        // 5. Create order
        const order = this.orderRepository.create({
            userId,
            subtotal,
            discount,
            totalAmount: subtotal - discount,
            status: OrderStatus.PENDING,
            voucherCode: dto.voucherCode,
        });

        // 6. Create order items + deduct stock
        order.items = cartItems.map(item => {
            // Deduct stock
            item.product.quantity -= item.quantity;
            this.productRepository.save(item.product);

            return this.orderItemRepository.create({
                product: item.product,
                quantity: item.quantity,
                unitPrice: item.product.price,
            });
        });

        const savedOrder = await this.orderRepository.save(order);

        // 7. Clear cart
        await this.cartItemRepository.delete({ userId });

        return savedOrder;
    }
}
```

### 3.3 Order Status Flow

```
PENDING → CONFIRMED → SHIPPING → DELIVERED
    │         │
    ▼         ▼
 CANCELLED  CANCELLED

Rules:
- Chỉ cancel khi PENDING hoặc CONFIRMED
- Cancel → hoàn lại stock
- ADMIN mới được chuyển status: CONFIRMED → SHIPPING → DELIVERED
- USER chỉ được cancel
```

```typescript
async updateStatus(orderId: number, newStatus: OrderStatus) {
    const order = await this.findOne(orderId);
    const validTransitions: Record<OrderStatus, OrderStatus[]> = {
        [OrderStatus.PENDING]: [OrderStatus.CONFIRMED, OrderStatus.CANCELLED],
        [OrderStatus.CONFIRMED]: [OrderStatus.SHIPPING, OrderStatus.CANCELLED],
        [OrderStatus.SHIPPING]: [OrderStatus.DELIVERED],
        [OrderStatus.DELIVERED]: [],
        [OrderStatus.CANCELLED]: [],
    };

    if (!validTransitions[order.status].includes(newStatus)) {
        throw new BadRequestException(
            `Không thể chuyển từ ${order.status} sang ${newStatus}`);
    }

    // Cancel → restore stock
    if (newStatus === OrderStatus.CANCELLED) {
        for (const item of order.items) {
            item.product.quantity += item.quantity;
            await this.productRepository.save(item.product);
        }
    }

    order.status = newStatus;
    return this.orderRepository.save(order);
}
```

---

## 4. EDGE CASES CẦN XỬ LÝ

### Banking
- Chuyển khoản cho chính mình → reject
- Rút quá số dư → reject với message rõ ràng
- Account bị freeze → reject mọi giao dịch
- Concurrent transfers (2 request cùng lúc) → pessimistic locking
- Amount = 0 hoặc negative → reject

### Ecommerce
- Add product đã hết hàng vào cart → reject
- Đặt hàng khi stock thay đổi giữa lúc thêm cart và checkout → re-check stock
- Cancel đơn đã shipping → reject
- Dùng voucher đã expired / hết lượt → reject
- Giỏ hàng trống → reject khi checkout

---

## 5. TÓM TẮT

Ngày 13 tập trung vào:
- Business logic phức tạp (fees, limits, vouchers)
- Workflow (order status transitions)
- Edge cases và validation kỹ lưỡng
- Mọi thứ phải test bằng Postman và pass
