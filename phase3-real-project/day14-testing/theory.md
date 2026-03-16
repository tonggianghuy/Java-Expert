# NGÀY 14: Testing, Documentation & Polish

---

## 1. TẠI SAO CẦN TESTING?

```
Không có test:
  "Code chạy đúng trên máy tôi" → Deploy → Bug → Fix → Tạo bug mới → Loop

Có test:
  Viết code → Chạy test → Pass → Deploy confident
                         → Fail → Fix trước khi deploy
```

### Trong Banking: Testing = BẮT BUỘC
- 1 bug = mất tiền thật của khách hàng
- Regulation yêu cầu test coverage
- Mỗi business rule PHẢI có test tương ứng

---

## 2. UNIT TEST - JAVA (JUnit 5 + Mockito)

### 2.1 Setup

```xml
<!-- Đã có sẵn trong spring-boot-starter-test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 2.2 Test Service Layer

```java
@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransferService transferService;

    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        sender = new Account();
        sender.setId(1L);
        sender.setAccountNumber("VCB001");
        sender.setBalance(new BigDecimal("10000000"));
        sender.setActive(true);

        receiver = new Account();
        receiver.setId(2L);
        receiver.setAccountNumber("VCB002");
        receiver.setBalance(new BigDecimal("5000000"));
        receiver.setActive(true);
    }

    @Test
    @DisplayName("Chuyển khoản thành công khi đủ điều kiện")
    void transfer_ShouldSucceed_WhenValidRequest() {
        // Given
        TransferRequest request = new TransferRequest(
            "VCB001", "VCB002", new BigDecimal("3000000"));

        when(accountRepository.findByAccountNumber("VCB001"))
            .thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002"))
            .thenReturn(Optional.of(receiver));
        when(transactionRepository.sumTodayTransfers(anyLong(), any()))
            .thenReturn(BigDecimal.ZERO);

        // When
        TransferResponse response = transferService.transfer(request);

        // Then
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(sender.getBalance()).isEqualByComparingTo("7000000");
        assertThat(receiver.getBalance()).isEqualByComparingTo("8000000");
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Chuyển khoản thất bại khi số dư không đủ")
    void transfer_ShouldThrow_WhenInsufficientFunds() {
        TransferRequest request = new TransferRequest(
            "VCB001", "VCB002", new BigDecimal("20000000"));

        when(accountRepository.findByAccountNumber("VCB001"))
            .thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002"))
            .thenReturn(Optional.of(receiver));

        assertThatThrownBy(() -> transferService.transfer(request))
            .isInstanceOf(InsufficientFundsException.class);

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Chuyển khoản thất bại khi vượt hạn mức ngày")
    void transfer_ShouldThrow_WhenDailyLimitExceeded() {
        TransferRequest request = new TransferRequest(
            "VCB001", "VCB002", new BigDecimal("3000000"));

        when(accountRepository.findByAccountNumber("VCB001"))
            .thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002"))
            .thenReturn(Optional.of(receiver));
        when(transactionRepository.sumTodayTransfers(anyLong(), any()))
            .thenReturn(new BigDecimal("499000000")); // Đã chuyển gần 500M

        assertThatThrownBy(() -> transferService.transfer(request))
            .isInstanceOf(DailyLimitExceededException.class);
    }

    @Test
    @DisplayName("Không thể chuyển khoản cho chính mình")
    void transfer_ShouldThrow_WhenSameAccount() {
        TransferRequest request = new TransferRequest(
            "VCB001", "VCB001", new BigDecimal("1000000"));

        assertThatThrownBy(() -> transferService.transfer(request))
            .isInstanceOf(InvalidTransferException.class);
    }

    @Test
    @DisplayName("Không thể chuyển khoản khi account bị freeze")
    void transfer_ShouldThrow_WhenAccountFrozen() {
        sender.setActive(false);
        TransferRequest request = new TransferRequest(
            "VCB001", "VCB002", new BigDecimal("1000000"));

        when(accountRepository.findByAccountNumber("VCB001"))
            .thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002"))
            .thenReturn(Optional.of(receiver));

        assertThatThrownBy(() -> transferService.transfer(request))
            .isInstanceOf(AccountInactiveException.class);
    }
}
```

### 2.3 Test Patterns

```java
// Mỗi test method theo pattern: Given - When - Then
@Test
void methodName_ShouldExpectedBehavior_WhenCondition() {
    // Given (Arrange): Setup data và mock
    // When (Act): Gọi method cần test
    // Then (Assert): Kiểm tra kết quả
}

// Test naming: methodName_ShouldXxx_WhenYyy
// deposit_ShouldIncreaseBalance_WhenValidAmount
// withdraw_ShouldThrow_WhenInsufficientFunds
// calculateFee_ShouldReturnMinFee_WhenAmountIsSmall
```

---

## 3. UNIT TEST - TYPESCRIPT (Jest)

### 3.1 Test Service

```typescript
describe('OrderService', () => {
  let orderService: OrderService;
  let cartItemRepository: Repository<CartItem>;
  let orderRepository: Repository<Order>;
  let productRepository: Repository<Product>;
  let voucherRepository: Repository<Voucher>;

  beforeEach(async () => {
    const module = await Test.createTestingModule({
      providers: [
        OrderService,
        { provide: getRepositoryToken(CartItem), useClass: MockRepository },
        { provide: getRepositoryToken(Order), useClass: MockRepository },
        { provide: getRepositoryToken(Product), useClass: MockRepository },
        { provide: getRepositoryToken(Voucher), useClass: MockRepository },
      ],
    }).compile();

    orderService = module.get(OrderService);
    cartItemRepository = module.get(getRepositoryToken(CartItem));
  });

  describe('placeOrder', () => {
    it('should create order successfully', async () => {
      // Arrange
      const cartItems = [
        { product: { id: 1, name: 'iPhone', price: 25990000, quantity: 50 },
          quantity: 1, userId: 1 },
      ];
      jest.spyOn(cartItemRepository, 'find').mockResolvedValue(cartItems as any);
      jest.spyOn(orderRepository, 'save').mockImplementation(async (order) => ({
        ...order, id: 1,
      } as any));

      // Act
      const result = await orderService.placeOrder(1, {});

      // Assert
      expect(result.status).toBe('PENDING');
      expect(result.totalAmount).toBe(25990000);
    });

    it('should throw when cart is empty', async () => {
      jest.spyOn(cartItemRepository, 'find').mockResolvedValue([]);

      await expect(orderService.placeOrder(1, {}))
        .rejects.toThrow('Giỏ hàng trống');
    });

    it('should throw when product is out of stock', async () => {
      const cartItems = [
        { product: { id: 1, name: 'iPhone', price: 25990000, quantity: 0 },
          quantity: 1, userId: 1 },
      ];
      jest.spyOn(cartItemRepository, 'find').mockResolvedValue(cartItems as any);

      await expect(orderService.placeOrder(1, {}))
        .rejects.toThrow(/chỉ còn/);
    });

    it('should apply voucher correctly', async () => {
      const cartItems = [
        { product: { id: 1, price: 1000000, quantity: 10 }, quantity: 2, userId: 1 },
      ];
      const voucher = {
        code: 'SALE10',
        type: 'PERCENTAGE',
        value: 10,
        minOrderValue: 500000,
        maxDiscount: 200000,
        expiryDate: new Date('2025-12-31'),
        usageLimit: 100,
        usedCount: 0,
      };

      jest.spyOn(cartItemRepository, 'find').mockResolvedValue(cartItems as any);
      jest.spyOn(voucherRepository, 'findOne').mockResolvedValue(voucher as any);

      const result = await orderService.placeOrder(1, { voucherCode: 'SALE10' });

      // 2 * 1000000 = 2000000, discount 10% = 200000 (= maxDiscount)
      expect(result.discount).toBe(200000);
      expect(result.totalAmount).toBe(1800000);
    });
  });
});
```

### 3.2 Chạy tests

```bash
# Java
mvn test                    # Chạy tất cả tests
mvn test -Dtest=TransferServiceTest  # Chạy 1 test class

# TypeScript
npm test                    # Chạy tất cả tests
npm test -- --watch         # Watch mode
npm test -- --coverage      # Với coverage report
npm test -- order.service   # Chạy tests cho 1 file
```

---

## 4. API DOCUMENTATION (Swagger/OpenAPI)

### 4.1 Java - SpringDoc

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

Truy cập: `http://localhost:8080/swagger-ui.html`

### 4.2 NestJS - @nestjs/swagger

```bash
npm install @nestjs/swagger swagger-ui-express
```

```typescript
// main.ts
const config = new DocumentBuilder()
    .setTitle('E-Commerce API')
    .setDescription('API documentation for E-Commerce Platform')
    .setVersion('1.0')
    .addBearerAuth()
    .build();
const document = SwaggerModule.createDocument(app, config);
SwaggerModule.setup('api/docs', app, document);
```

Truy cập: `http://localhost:3000/api/docs`

---

## 5. CHECKLIST NGÀY 14

```
Testing:
□ Unit tests cho TransferService (>= 5 test cases)
□ Unit tests cho OrderService (>= 5 test cases)
□ Unit tests cho FeeCalculator
□ Unit tests cho Voucher validation
□ Tất cả tests PASS
□ Coverage >= 70% cho service layer

Documentation:
□ Swagger UI chạy được cho cả 2 project
□ README.md hoàn chỉnh (setup, run, API overview)

Polish:
□ Xóa code thừa, TODO comments
□ Consistent error messages
□ Consistent response format
□ Không còn known bugs
```
