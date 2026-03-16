# NGÀY 3: OOP - Object Oriented Programming

---

## 1. TƯ DUY OOP

### 1.1 Lập trình hướng đối tượng là gì?

OOP là cách **mô hình hóa thế giới thực** bằng code. Thay vì viết code theo trình tự (procedural), ta tổ chức code thành các **đối tượng (objects)** có:
- **Thuộc tính (attributes/fields):** dữ liệu mà object chứa
- **Hành vi (methods):** việc mà object có thể làm

### 1.2 Ví dụ tư duy

```
Thế giới thực                    Code
─────────────                    ────
Tài khoản ngân hàng    →    class BankAccount
├── Số tài khoản       →        String accountNumber
├── Tên chủ TK         →        String ownerName
├── Số dư              →        double balance
├── Nạp tiền           →        void deposit(amount)
├── Rút tiền           →        boolean withdraw(amount)
└── Xem số dư          →        double getBalance()

Sản phẩm               →    class Product
├── Mã SP              →        String productId
├── Tên                →        String name
├── Giá                →        double price
├── Giảm giá           →        void applyDiscount(percent)
└── Hiển thị           →        String toString()
```

### 1.3 Class vs Object

```
Class = Bản thiết kế (Blueprint)
Object = Sản phẩm thực tế tạo từ blueprint

Class BankAccount (blueprint)         Objects (instances)
┌──────────────────────┐     ┌──────────────────────────┐
│ accountNumber        │     │ acc1:                     │
│ ownerName            │ ──→ │   accountNumber = "001"   │
│ balance              │     │   ownerName = "Anh"       │
│                      │     │   balance = 5,000,000     │
│ deposit()            │     └──────────────────────────┘
│ withdraw()           │     ┌──────────────────────────┐
│ getBalance()         │ ──→ │ acc2:                     │
└──────────────────────┘     │   accountNumber = "002"   │
                             │   ownerName = "Binh"      │
1 class                      │   balance = 10,000,000    │
→ nhiều objects               └──────────────────────────┘
```

---

## 2. BỐN TRỤ CỘT CỦA OOP

```
┌─────────────────────────────────────────────────────────┐
│                    4 PILLARS OF OOP                      │
├──────────────┬──────────────┬─────────────┬─────────────┤
│ ENCAPSULATION│ INHERITANCE  │ POLYMORPHISM│ ABSTRACTION │
│              │              │             │             │
│ Đóng gói    │ Kế thừa      │ Đa hình     │ Trừu tượng │
│              │              │             │             │
│ Bảo vệ data │ Tái sử dụng  │ Cùng method │ Ẩn chi tiết│
│ private +   │ code từ      │ hành vi     │ chỉ show   │
│ getter/setter│ class cha    │ khác nhau   │ interface   │
└──────────────┴──────────────┴─────────────┴─────────────┘
```

---

## 3. CLASS, OBJECT & CONSTRUCTOR

### 3.1 Tạo Class

```java
public class Product {
    // Fields (thuộc tính)
    String name;
    double price;
    int quantity;
    String category;

    // Method (hành vi)
    void displayInfo() {
        System.out.printf("%-20s | %,.0f VND | SL: %d%n", name, price, quantity);
    }

    double getTotalValue() {
        return price * quantity;
    }
}
```

### 3.2 Tạo Object

```java
public class Main {
    public static void main(String[] args) {
        // Tạo object bằng từ khóa "new"
        Product iphone = new Product();
        iphone.name = "iPhone 15";
        iphone.price = 25990000;
        iphone.quantity = 50;
        iphone.category = "Electronics";

        Product macbook = new Product();
        macbook.name = "MacBook Pro";
        macbook.price = 49990000;
        macbook.quantity = 20;
        macbook.category = "Electronics";

        // Gọi method
        iphone.displayInfo();
        macbook.displayInfo();

        System.out.printf("Tổng giá trị kho iPhone: %,.0f VND%n", iphone.getTotalValue());
    }
}
```

### 3.3 Constructor - Hàm khởi tạo

Constructor là method đặc biệt được gọi khi tạo object bằng `new`.

```java
public class BankAccount {
    String accountNumber;
    String ownerName;
    double balance;

    // Constructor mặc định (no-arg)
    public BankAccount() {
        this.balance = 0;
    }

    // Constructor có tham số
    public BankAccount(String accountNumber, String ownerName) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = 0;
    }

    // Constructor đầy đủ
    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
    }
}

// Sử dụng:
BankAccount acc1 = new BankAccount();                              // balance = 0
BankAccount acc2 = new BankAccount("VCB001", "Nguyen Van A");      // balance = 0
BankAccount acc3 = new BankAccount("VCB002", "Tran Thi B", 5000000); // balance = 5M
```

### 3.4 `this` keyword

`this` tham chiếu đến **object hiện tại**.

```java
public class Customer {
    private String name;
    private String email;

    public Customer(String name, String email) {
        this.name = name;     // this.name = field, name = parameter
        this.email = email;
    }

    // this để gọi constructor khác
    public Customer(String name) {
        this(name, "unknown@email.com");  // Gọi constructor 2 tham số
    }
}
```

---

## 4. ENCAPSULATION (Đóng gói)

### 4.1 Tại sao cần Encapsulation?

```java
// ❌ KHÔNG có encapsulation - AI CŨNG CÓ THỂ SỬA balance
class BadAccount {
    double balance = 5000000;
}

BadAccount acc = new BadAccount();
acc.balance = -999999999;  // Set số dư âm! Không có ai kiểm tra!
```

```java
// ✅ CÓ encapsulation - Chỉ sửa qua method (có validation)
class GoodAccount {
    private double balance = 5000000;  // private: chỉ class này truy cập được

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền phải > 0");
        }
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;  // Từ chối
        }
        balance -= amount;
        return true;
    }
}
```

### 4.2 Access Modifiers

| Modifier | Same Class | Same Package | Subclass | Everywhere |
|----------|-----------|-------------|----------|-----------|
| `private` | ✅ | ❌ | ❌ | ❌ |
| *(default)* | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

**Quy tắc vàng:**
- Fields: luôn `private`
- Getter/Setter: `public`
- Helper methods: `private`
- Methods cho bên ngoài: `public`

### 4.3 Getter & Setter

```java
public class Customer {
    private String name;
    private String email;
    private String phone;

    // Getter - đọc giá trị
    public String getName() {
        return name;
    }

    // Setter - ghi giá trị (CÓ validation)
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên không được rỗng");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        this.email = email.toLowerCase();
    }

    // Getter cho phone: che bớt số
    public String getPhone() {
        if (phone == null) return null;
        return "****" + phone.substring(phone.length() - 4);  // ****6789
    }

    // Setter cho phone
    public void setPhone(String phone) {
        if (phone == null || phone.length() != 10) {
            throw new IllegalArgumentException("Số điện thoại phải 10 số");
        }
        this.phone = phone;
    }
}
```

---

## 5. INHERITANCE (Kế thừa)

### 5.1 Khái niệm

Inheritance cho phép tạo class mới **dựa trên class đã có**, kế thừa fields và methods.

```
        BankAccount (Parent/Super class)
        ├── accountNumber
        ├── ownerName
        ├── balance
        ├── deposit()
        └── withdraw()
              │
    ┌─────────┼───────────┐
    │         │           │
SavingsAccount  CheckingAccount  FixedDeposit
├── interestRate  ├── overdraftLimit  ├── termMonths
├── calculateInterest()  │             ├── maturityDate
                  └── withdraw()       └── earlyWithdrawPenalty()
                    (override)
```

### 5.2 extends keyword

```java
// Parent class
public class BankAccount {
    protected String accountNumber;  // protected: subclass truy cập được
    protected String ownerName;
    protected double balance;

    public BankAccount(String accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Nạp %,.0f. Số dư: %,.0f%n", amount, balance);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.printf("Rút %,.0f. Số dư: %,.0f%n", amount, balance);
            return true;
        }
        System.out.println("Giao dịch thất bại!");
        return false;
    }

    public double getBalance() { return balance; }
}

// Child class
public class SavingsAccount extends BankAccount {
    private double interestRate;  // Field riêng

    public SavingsAccount(String accountNumber, String ownerName,
                          double balance, double interestRate) {
        super(accountNumber, ownerName, balance);  // Gọi constructor cha
        this.interestRate = interestRate;
    }

    // Method riêng
    public double calculateMonthlyInterest() {
        return balance * interestRate / 12;
    }

    public void applyInterest() {
        double interest = calculateMonthlyInterest();
        deposit(interest);  // Gọi method kế thừa từ cha
        System.out.printf("Lãi tháng: %,.0f VND%n", interest);
    }
}
```

### 5.3 super keyword

```java
public class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String ownerName,
                           double balance, double overdraftLimit) {
        super(accountNumber, ownerName, balance);  // super() = gọi constructor cha
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        // Checking cho phép rút quá số dư (trong hạn mức)
        if (amount > 0 && amount <= balance + overdraftLimit) {
            balance -= amount;
            System.out.printf("Rút %,.0f. Số dư: %,.0f%n", amount, balance);
            if (balance < 0) {
                System.out.printf("⚠️ Thấu chi: %,.0f / Hạn mức: %,.0f%n",
                    Math.abs(balance), overdraftLimit);
            }
            return true;
        }
        System.out.println("Vượt quá hạn mức thấu chi!");
        return false;
    }
}
```

### 5.4 Quy tắc kế thừa

1. Java chỉ hỗ trợ **single inheritance** (1 class chỉ extends 1 class)
2. Constructor KHÔNG được kế thừa → phải gọi `super()` trong constructor con
3. `private` members KHÔNG được kế thừa (nhưng vẫn tồn tại, truy cập qua getter)
4. `super()` phải là dòng ĐẦU TIÊN trong constructor con

---

## 6. POLYMORPHISM (Đa hình)

### 6.1 Khái niệm

Cùng một method name nhưng **hành vi khác nhau** tùy vào object thực tế.

### 6.2 Method Overriding (Runtime Polymorphism)

```java
BankAccount acc1 = new SavingsAccount("001", "A", 10000000, 0.055);
BankAccount acc2 = new CheckingAccount("002", "B", 5000000, 2000000);

// Cùng gọi withdraw() nhưng hành vi KHÁC NHAU
acc1.withdraw(15000000);  // SavingsAccount: Thất bại (không đủ tiền)
acc2.withdraw(6000000);   // CheckingAccount: Thành công (dùng overdraft)

// Polymorphism mạnh nhất khi dùng với Collection
ArrayList<BankAccount> accounts = new ArrayList<>();
accounts.add(new SavingsAccount("001", "A", 10000000, 0.055));
accounts.add(new CheckingAccount("002", "B", 5000000, 2000000));
accounts.add(new SavingsAccount("003", "C", 20000000, 0.065));

// Duyệt - mỗi account tự biết cách withdraw riêng
for (BankAccount acc : accounts) {
    System.out.printf("Tài khoản %s (%s): %,.0f VND%n",
        acc.accountNumber,
        acc.getClass().getSimpleName(),
        acc.getBalance());
}
```

### 6.3 Method Overloading (Compile-time Polymorphism)

Cùng tên method nhưng **khác tham số**.

```java
public class PaymentProcessor {
    // Thanh toán bằng tiền mặt
    public void processPayment(double amount) {
        System.out.printf("Thanh toán tiền mặt: %,.0f VND%n", amount);
    }

    // Thanh toán bằng thẻ
    public void processPayment(double amount, String cardNumber) {
        System.out.printf("Thanh toán thẻ ****%s: %,.0f VND%n",
            cardNumber.substring(cardNumber.length() - 4), amount);
    }

    // Thanh toán bằng ví điện tử
    public void processPayment(double amount, String walletType, String phone) {
        System.out.printf("Thanh toán %s (%s): %,.0f VND%n", walletType, phone, amount);
    }
}

PaymentProcessor processor = new PaymentProcessor();
processor.processPayment(500000);                            // Tiền mặt
processor.processPayment(500000, "4111222233334444");         // Thẻ
processor.processPayment(500000, "MoMo", "0901234567");      // Ví
```

### 6.4 @Override annotation

```java
public class SavingsAccount extends BankAccount {

    @Override  // ← Đánh dấu: method này GHI ĐÈ method của cha
    public boolean withdraw(double amount) {
        // Logic riêng cho Savings
        return super.withdraw(amount);  // Có thể gọi logic cha nếu cần
    }

    // ⚠️ @Override giúp compiler kiểm tra
    // Nếu viết sai tên method → compiler báo lỗi
    @Override
    public boolean withdrow(double amount) {  // ❌ Typo! Compiler báo lỗi
        return false;
    }
}
```

---

## 7. ABSTRACTION (Trừu tượng hóa)

### 7.1 Abstract Class

Abstract class **không thể tạo object trực tiếp**. Nó là "bản thiết kế chưa hoàn chỉnh".

```java
// Không thể: new BankAccount() ← ❌ nếu BankAccount là abstract
public abstract class BankAccount {
    protected String accountNumber;
    protected double balance;

    // Concrete method - có body, subclass kế thừa ngay
    public void deposit(double amount) {
        balance += amount;
    }

    // Abstract method - KHÔNG có body, subclass BẮT BUỘC phải implement
    public abstract double calculateInterest();
    public abstract String getAccountType();
    public abstract double getMaxWithdrawLimit();
}

// Subclass BẮT BUỘC implement tất cả abstract methods
public class SavingsAccount extends BankAccount {
    private double interestRate;

    @Override
    public double calculateInterest() {
        return balance * interestRate / 12;  // Lãi theo tháng
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    @Override
    public double getMaxWithdrawLimit() {
        return balance;  // Rút tối đa = số dư
    }
}

public class CheckingAccount extends BankAccount {
    private double overdraftLimit;

    @Override
    public double calculateInterest() {
        return 0;  // Checking không có lãi
    }

    @Override
    public String getAccountType() {
        return "CHECKING";
    }

    @Override
    public double getMaxWithdrawLimit() {
        return balance + overdraftLimit;  // Rút tối đa = số dư + thấu chi
    }
}
```

### 7.2 Interface

Interface là **hợp đồng (contract)** - định nghĩa "class phải làm được gì" nhưng không nói "làm như thế nào".

```java
// Interface: "Tôi cam kết có thể thanh toán được"
public interface Payable {
    double calculateTotal();
    void processPayment();
}

// Interface: "Tôi cam kết có thể giảm giá"
public interface Discountable {
    double applyDiscount(double percent);
    boolean isEligibleForDiscount();
}

// Interface: "Tôi cam kết có thể in ra"
public interface Printable {
    void printReceipt();
}

// 1 class có thể implement NHIỀU interface
public class Order implements Payable, Discountable, Printable {
    private String orderId;
    private double amount;

    @Override
    public double calculateTotal() {
        return amount;
    }

    @Override
    public void processPayment() {
        System.out.println("Đang xử lý thanh toán đơn hàng " + orderId);
    }

    @Override
    public double applyDiscount(double percent) {
        double discount = amount * percent / 100;
        amount -= discount;
        return discount;
    }

    @Override
    public boolean isEligibleForDiscount() {
        return amount >= 500000;
    }

    @Override
    public void printReceipt() {
        System.out.printf("Hóa đơn %s: %,.0f VND%n", orderId, amount);
    }
}
```

### 7.3 Abstract Class vs Interface

| Tiêu chí | Abstract Class | Interface |
|----------|---------------|-----------|
| Keyword | `extends` | `implements` |
| Multiple | Chỉ extend 1 | Implement nhiều |
| Fields | Có instance fields | Chỉ constants (`static final`) |
| Constructor | Có | Không |
| Methods | Abstract + concrete | Mặc định abstract (Java 8+: default methods) |
| Khi nào dùng? | Các class có **quan hệ IS-A** rõ ràng | Định nghĩa **khả năng** (CAN-DO) |

```java
// IS-A: SavingsAccount IS A BankAccount
public class SavingsAccount extends BankAccount { }

// CAN-DO: Order CAN DO payment, CAN DO discount
public class Order implements Payable, Discountable { }
```

### 7.4 Interface với Default Methods (Java 8+)

```java
public interface Transferable {
    boolean transfer(BankAccount target, double amount);

    // Default method: có body sẵn, class không bắt buộc override
    default String getTransferDescription(double amount) {
        return String.format("Chuyển khoản %,.0f VND", amount);
    }

    // Static method trong interface
    static double calculateFee(double amount) {
        return amount * 0.001;  // 0.1% phí
    }
}
```

---

## 8. STATIC KEYWORD

### 8.1 static field và method

`static` thuộc về **class**, không thuộc về object cụ thể.

```java
public class BankAccount {
    // Static field: chia sẻ giữa TẤT CẢ objects
    private static int totalAccounts = 0;
    private static final String BANK_NAME = "MY BANK";

    // Instance field: mỗi object có giá trị riêng
    private String accountNumber;
    private double balance;

    public BankAccount(String ownerName) {
        totalAccounts++;
        this.accountNumber = BANK_NAME + "-" + String.format("%04d", totalAccounts);
    }

    // Static method: gọi bằng ClassName.method()
    public static int getTotalAccounts() {
        return totalAccounts;
    }

    // Static method KHÔNG thể truy cập instance field
    // public static double getBalance() { return balance; }  // ❌ ERROR
}

// Sử dụng:
BankAccount acc1 = new BankAccount("A");
BankAccount acc2 = new BankAccount("B");
System.out.println(BankAccount.getTotalAccounts());  // 2 (gọi qua class name)
```

### 8.2 Utility class pattern

```java
public class CurrencyFormatter {
    // Private constructor: không cho tạo object
    private CurrencyFormatter() {}

    public static String formatVND(double amount) {
        return String.format("%,.0f VND", amount);
    }

    public static String formatUSD(double amount) {
        return String.format("$%,.2f", amount);
    }

    public static double vndToUsd(double vnd) {
        return vnd / 24500;
    }
}

// Sử dụng (không cần new)
System.out.println(CurrencyFormatter.formatVND(15000000));  // "15,000,000 VND"
```

---

## 9. TỔNG HỢP: VÍ DỤ HOÀN CHỈNH

```java
// Interface
public interface Auditable {
    String getAuditLog();
}

// Abstract class
public abstract class BankAccount implements Auditable {
    private static int counter = 0;
    protected final String accountNumber;
    protected String ownerName;
    protected double balance;

    public BankAccount(String ownerName, double initialBalance) {
        this.accountNumber = "ACC" + String.format("%06d", ++counter);
        this.ownerName = ownerName;
        this.balance = initialBalance;
    }

    public abstract double calculateInterest();
    public abstract String getAccountType();

    public void deposit(double amount) { balance += amount; }

    public boolean withdraw(double amount) {
        if (amount <= balance) { balance -= amount; return true; }
        return false;
    }

    @Override
    public String getAuditLog() {
        return String.format("[%s] %s | Owner: %s | Balance: %,.0f",
            getAccountType(), accountNumber, ownerName, balance);
    }

    public double getBalance() { return balance; }
}

// Concrete class
public class SavingsAccount extends BankAccount {
    private final double annualRate;

    public SavingsAccount(String owner, double balance, double rate) {
        super(owner, balance);
        this.annualRate = rate;
    }

    @Override
    public double calculateInterest() { return balance * annualRate / 12; }

    @Override
    public String getAccountType() { return "SAVINGS"; }
}

// Usage with Polymorphism
ArrayList<BankAccount> accounts = new ArrayList<>();
accounts.add(new SavingsAccount("Anh", 10000000, 0.055));
accounts.add(new CheckingAccount("Binh", 5000000, 2000000));

for (BankAccount acc : accounts) {
    System.out.println(acc.getAuditLog());                 // Polymorphism
    System.out.printf("  Lãi: %,.0f%n", acc.calculateInterest()); // Polymorphism
}
```

---

## 10. TÓM TẮT NGÀY 3

```
┌─────────────────────────────────────────────────┐
│              NGÀY 3 - TÓM TẮT                  │
├─────────────────────────────────────────────────┤
│ Class & Object:                                 │
│ ✓ Class = blueprint, Object = instance          │
│ ✓ Constructor khởi tạo object                   │
│ ✓ this = tham chiếu object hiện tại            │
│                                                 │
│ 4 Pillars:                                      │
│ ✓ Encapsulation: private + getter/setter        │
│ ✓ Inheritance: extends, super, code reuse       │
│ ✓ Polymorphism: override + overload             │
│ ✓ Abstraction: abstract class + interface       │
│                                                 │
│ Quy tắc:                                       │
│ ✓ Fields luôn private                           │
│ ✓ Java: single inheritance, multiple interfaces │
│ ✓ abstract class = IS-A, interface = CAN-DO     │
│ ✓ @Override để compiler kiểm tra                │
│ ✓ static thuộc về class, không thuộc object     │
└─────────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. Class và Object khác nhau thế nào?
2. Tại sao fields nên đặt `private`? Nếu không private thì sao?
3. `extends` và `implements` khác nhau gì?
4. Method overriding và overloading khác nhau thế nào?
5. Khi nào dùng abstract class, khi nào dùng interface?
6. `static` method có thể truy cập instance field không? Tại sao?
7. Trong banking, hãy cho ví dụ về polymorphism.
