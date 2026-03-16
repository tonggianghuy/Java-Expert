# NGÀY 1: Java Fundamentals & Tư Duy Lập Trình

---

## 1. JAVA LÀ GÌ? TẠI SAO BANKING CHỌN JAVA?

### 1.1 Lịch sử ngắn gọn
- Được tạo bởi **James Gosling** tại Sun Microsystems (1995)
- Triết lý: **"Write Once, Run Anywhere"** (WORA)
- Hiện thuộc sở hữu của Oracle

### 1.2 Tại sao Banking & Enterprise chọn Java?

| Lý do | Giải thích |
|-------|-----------|
| **Ổn định** | Java đã tồn tại 30+ năm, cộng đồng khổng lồ |
| **Bảo mật** | Type-safe, memory management tự động, không có pointer trực tiếp |
| **Hiệu năng** | JIT compilation tối ưu runtime, xử lý hàng triệu giao dịch/ngày |
| **Ecosystem** | Spring Framework, Hibernate, hàng nghìn thư viện |
| **Backward Compatible** | Code Java 8 vẫn chạy trên Java 17+ |
| **Multi-threading** | Xử lý đồng thời nhiều giao dịch cùng lúc |

> **Thực tế:** Các ngân hàng lớn (JPMorgan, Goldman Sachs, HSBC, Vietcombank, Techcombank...) đều dùng Java cho core banking system.

### 1.3 JDK vs JRE vs JVM

```
┌─────────────────────────────────────────────┐
│ JDK (Java Development Kit)                  │
│ ┌─────────────────────────────────────────┐ │
│ │ JRE (Java Runtime Environment)          │ │
│ │ ┌─────────────────────────────────────┐ │ │
│ │ │ JVM (Java Virtual Machine)          │ │ │
│ │ │                                     │ │ │
│ │ │ Chạy bytecode (.class)              │ │ │
│ │ │ Quản lý memory (Garbage Collector)  │ │ │
│ │ │ Platform independent                │ │ │
│ │ └─────────────────────────────────────┘ │ │
│ │ + Java Class Libraries (rt.jar)         │ │
│ │ + Java API                              │ │
│ └─────────────────────────────────────────┘ │
│ + javac (compiler)                          │
│ + java (launcher)                           │
│ + javadoc, jar, jdb (debugger)              │
└─────────────────────────────────────────────┘
```

**Giải thích đơn giản:**
- **JVM:** Máy ảo chạy chương trình Java. Giống như "cái máy chiếu phim"
- **JRE:** JVM + thư viện cần thiết để chạy. Giống "máy chiếu + phim có sẵn"
- **JDK:** JRE + công cụ phát triển. Giống "máy chiếu + phim + camera để quay phim mới"

> **Developer cần JDK.** Người dùng cuối chỉ cần JRE.

### 1.4 Quy trình chạy chương trình Java

```
Source Code (.java)
        │
        ▼ javac (compiler)
Bytecode (.class)
        │
        ▼ JVM
Machine Code (chạy trên OS)
```

```java
// File: HelloWorld.java
public class HelloWorld {          // 1. Viết code
    public static void main(String[] args) {
        System.out.println("Hello Banking World!");
    }
}
// 2. Compile: javac HelloWorld.java  →  HelloWorld.class
// 3. Run:     java HelloWorld        →  "Hello Banking World!"
```

---

## 2. CÀI ĐẶT MÔI TRƯỜNG

### 2.1 Cài JDK 17+ (LTS)

**Windows:**
1. Tải từ https://adoptium.net/ (Temurin JDK 17)
2. Cài đặt, nhớ tick "Set JAVA_HOME"
3. Kiểm tra: mở Command Prompt → `java -version`

**macOS:**
```bash
brew install openjdk@17
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**Kiểm tra cài đặt thành công:**
```bash
java -version
# openjdk version "17.0.x" ...

javac -version
# javac 17.0.x
```

### 2.2 Cài IntelliJ IDEA

1. Tải **IntelliJ IDEA Community Edition** (miễn phí) từ https://www.jetbrains.com/idea/
2. Cài đặt → Mở → New Project → Java → Chọn JDK 17
3. Tạo file mới: `src/Main.java`

### 2.3 Cấu trúc project cơ bản

```
my-first-project/
├── src/
│   └── Main.java          ← Code của bạn
├── out/
│   └── Main.class          ← Bytecode (tự động tạo)
└── .idea/                   ← Config IntelliJ (tự động)
```

---

## 3. CẤU TRÚC CHƯƠNG TRÌNH JAVA

### 3.1 Anatomy of a Java Program

```java
// 1. Package declaration (tùy chọn - sẽ học sau)
// package com.banking.app;

// 2. Import statements (khi cần dùng thư viện)
import java.util.Scanner;

// 3. Class declaration - MỌI code Java phải nằm trong class
public class MyFirstProgram {

    // 4. Main method - điểm bắt đầu chương trình
    public static void main(String[] args) {

        // 5. Statements - các câu lệnh thực thi
        System.out.println("Xin chào! Tôi đang học Java!");
    }
}
```

### 3.2 Quy tắc bắt buộc

| Quy tắc | Ví dụ đúng | Ví dụ sai |
|---------|-----------|----------|
| Tên file = tên class | `BankAccount.java` chứa `class BankAccount` | `bank.java` chứa `class BankAccount` |
| Mỗi statement kết thúc bằng `;` | `int x = 5;` | `int x = 5` |
| Block code dùng `{ }` | `if (x > 0) { ... }` | |
| Java phân biệt hoa/thường | `String` ≠ `string` | |

### 3.3 Naming Conventions (Quy ước đặt tên)

```java
// Class: PascalCase (viết hoa chữ đầu mỗi từ)
public class BankAccount { }
public class ShoppingCart { }

// Method & Variable: camelCase (chữ đầu tiên viết thường)
int accountBalance = 1000000;
String customerName = "Nguyen Van A";
void calculateInterest() { }

// Constant: UPPER_SNAKE_CASE
final double MAX_TRANSFER_LIMIT = 500000000;
final int MIN_PASSWORD_LENGTH = 8;

// Package: lowercase
// package com.banking.service;
```

---

## 4. BIẾN VÀ KIỂU DỮ LIỆU

### 4.1 Biến (Variable) là gì?

Biến giống như một **chiếc hộp có nhãn** dùng để chứa dữ liệu.

```java
// Khai báo biến: kiểu_dữ_liệu tên_biến = giá_trị;
int age = 25;                    // hộp tên "age" chứa số 25
String name = "Nguyen Van A";    // hộp tên "name" chứa text
double balance = 15000000.50;    // hộp tên "balance" chứa số thập phân
boolean isActive = true;         // hộp tên "isActive" chứa true/false
```

### 4.2 Primitive Types (Kiểu dữ liệu nguyên thủy)

| Kiểu | Kích thước | Phạm vi | Ví dụ trong Banking |
|------|-----------|---------|-------------------|
| `byte` | 1 byte | -128 → 127 | Ít dùng |
| `short` | 2 bytes | -32,768 → 32,767 | Ít dùng |
| `int` | 4 bytes | -2.1 tỷ → 2.1 tỷ | Số lượng sản phẩm, tuổi |
| `long` | 8 bytes | Rất lớn | Mã giao dịch, timestamp |
| `float` | 4 bytes | ~7 chữ số thập phân | **Không dùng cho tiền!** |
| `double` | 8 bytes | ~15 chữ số thập phân | **Không dùng cho tiền!** |
| `char` | 2 bytes | 1 ký tự Unicode | Giới tính: 'M', 'F' |
| `boolean` | 1 bit | true / false | Tài khoản active? |

### 4.3 Reference Types (Kiểu tham chiếu)

```java
// String - chuỗi ký tự (KHÔNG phải primitive)
String customerName = "Tran Thi B";
String accountNumber = "0123456789";

// Array - mảng (sẽ học ngày 2)
int[] scores = {90, 85, 78};

// Object - đối tượng (sẽ học ngày 3)
// BankAccount myAccount = new BankAccount();
```

### 4.4 Khai báo biến - Các cách

```java
// Cách 1: Khai báo + gán giá trị
int quantity = 10;

// Cách 2: Khai báo trước, gán sau
double price;
price = 299000.0;

// Cách 3: Khai báo nhiều biến cùng kiểu
int x = 1, y = 2, z = 3;

// Cách 4: Hằng số (không thay đổi được)
final double TAX_RATE = 0.08;    // 8% VAT
final String BANK_NAME = "MY BANK";
// TAX_RATE = 0.10;  // ❌ ERROR! Cannot assign to final variable
```

### 4.5 Type Casting (Chuyển đổi kiểu)

```java
// Widening (tự động) - nhỏ → lớn: an toàn
int myInt = 100;
long myLong = myInt;       // int → long: OK
double myDouble = myLong;  // long → double: OK

// Narrowing (thủ công) - lớn → nhỏ: có thể mất data
double price = 299999.99;
int roundedPrice = (int) price;  // 299999 (mất .99)

// String → Number
String input = "150000";
int amount = Integer.parseInt(input);        // "150000" → 150000
double rate = Double.parseDouble("5.5");     // "5.5" → 5.5

// Number → String
String text = String.valueOf(amount);        // 150000 → "150000"
String text2 = "" + amount;                  // Cách nhanh (ít recommended)
```

> **⚠️ Banking Rule:** Dùng `BigDecimal` cho tiền, KHÔNG dùng `double`. Sẽ học chi tiết ở Phase 2.

---

## 5. OPERATORS (Toán tử)

### 5.1 Arithmetic Operators (Toán tử số học)

```java
int a = 10, b = 3;

System.out.println(a + b);   // 13   - Cộng
System.out.println(a - b);   // 7    - Trừ
System.out.println(a * b);   // 30   - Nhân
System.out.println(a / b);   // 3    - Chia (int/int = int, bỏ phần dư!)
System.out.println(a % b);   // 1    - Chia lấy dư (modulo)

// ⚠️ Cẩn thận với phép chia số nguyên!
System.out.println(10 / 3);    // 3 (không phải 3.33!)
System.out.println(10.0 / 3);  // 3.3333... (có ít nhất 1 double → kết quả double)

// Increment / Decrement
int count = 5;
count++;    // count = 6 (tăng 1)
count--;    // count = 5 (giảm 1)

// Compound assignment
int total = 100;
total += 50;   // total = 150  (tương đương total = total + 50)
total -= 30;   // total = 120
total *= 2;    // total = 240
total /= 4;    // total = 60
```

### 5.2 Comparison Operators (Toán tử so sánh)

```java
int balance = 5000000;
int withdrawAmount = 3000000;

System.out.println(balance > withdrawAmount);   // true
System.out.println(balance < withdrawAmount);   // false
System.out.println(balance >= 5000000);         // true
System.out.println(balance <= 4999999);         // false
System.out.println(balance == 5000000);         // true  (so sánh bằng: ==)
System.out.println(balance != 3000000);         // true  (so sánh khác: !=)

// ⚠️ So sánh String: PHẢI dùng .equals(), KHÔNG dùng ==
String pin1 = "1234";
String pin2 = "1234";
System.out.println(pin1 == pin2);        // Có thể true hoặc false! (so sánh reference)
System.out.println(pin1.equals(pin2));   // true (so sánh giá trị) ✅
```

### 5.3 Logical Operators (Toán tử logic)

```java
boolean hasEnoughBalance = true;
boolean isAccountActive = true;
boolean isBlacklisted = false;

// AND (&&): Cả hai phải true
boolean canWithdraw = hasEnoughBalance && isAccountActive;  // true

// OR (||): Ít nhất một true
boolean needsReview = isBlacklisted || !isAccountActive;    // false

// NOT (!): Đảo ngược
boolean isNotBlacklisted = !isBlacklisted;  // true

// Ví dụ thực tế: Kiểm tra điều kiện rút tiền
int balance2 = 5000000;
int amount = 3000000;
boolean accountActive = true;
int dailyWithdrawn = 400000000; // đã rút 400M trong ngày
int dailyLimit = 500000000;     // giới hạn 500M/ngày

boolean canProcess = accountActive
    && (balance2 >= amount)
    && (dailyWithdrawn + amount <= dailyLimit);
// true && true && true = true → Cho phép rút
```

---

## 6. INPUT / OUTPUT

### 6.1 Output - In ra màn hình

```java
// println - in và xuống dòng
System.out.println("Xin chào!");
System.out.println("Số dư: 5,000,000 VND");

// print - in KHÔNG xuống dòng
System.out.print("Nhập tên: ");  // con trỏ nằm cùng dòng

// printf - in có format (quan trọng!)
String name = "Nguyen Van A";
double balance = 15750000.5;
int transactions = 42;

System.out.printf("Khách hàng: %s%n", name);          // %s = String
System.out.printf("Số dư: %,.2f VND%n", balance);     // %,.2f = số có dấu phẩy, 2 chữ thập phân
System.out.printf("Giao dịch: %d lần%n", transactions); // %d = số nguyên
System.out.printf("Số dư: %,.0f VND%n", balance);     // %,.0f = không có thập phân

// Kết quả:
// Khách hàng: Nguyen Van A
// Số dư: 15,750,000.50 VND
// Giao dịch: 42 lần
// Số dư: 15,750,001 VND
```

**Bảng format specifier thường dùng:**

| Specifier | Ý nghĩa | Ví dụ |
|-----------|---------|-------|
| `%s` | String | `"Hello"` |
| `%d` | Integer | `42` |
| `%f` | Float/Double | `3.140000` |
| `%.2f` | 2 chữ thập phân | `3.14` |
| `%,.0f` | Có dấu phẩy, 0 thập phân | `1,000,000` |
| `%n` | Xuống dòng (cross-platform) | |
| `%10s` | Căn phải, rộng 10 ký tự | `"     Hello"` |
| `%-10s` | Căn trái, rộng 10 ký tự | `"Hello     "` |

### 6.2 Input - Nhận dữ liệu từ bàn phím

```java
import java.util.Scanner;  // Phải import!

public class InputDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Đọc String
        System.out.print("Nhập họ tên: ");
        String fullName = scanner.nextLine();   // đọc cả dòng

        // Đọc int
        System.out.print("Nhập tuổi: ");
        int age = scanner.nextInt();

        // Đọc double
        System.out.print("Nhập số tiền gửi: ");
        double amount = scanner.nextDouble();

        // ⚠️ BẪY PHỔ BIẾN: nextInt()/nextDouble() không đọc ký tự Enter
        scanner.nextLine(); // <-- Thêm dòng này để "ăn" ký tự Enter thừa

        System.out.print("Nhập ghi chú: ");
        String note = scanner.nextLine();  // Giờ mới đọc đúng

        // In kết quả
        System.out.printf("%nKhách hàng: %s, %d tuổi%n", fullName, age);
        System.out.printf("Số tiền gửi: %,.0f VND%n", amount);
        System.out.printf("Ghi chú: %s%n", note);

        scanner.close();  // Đóng scanner khi xong
    }
}
```

> **⚠️ Bẫy Scanner:** Sau khi gọi `nextInt()` hoặc `nextDouble()`, luôn gọi `scanner.nextLine()` trước khi đọc `nextLine()` tiếp theo. Nếu không, chương trình sẽ bỏ qua input.

---

## 7. CÂU LỆNH ĐIỀU KIỆN (Conditional Statements)

### 7.1 if / else if / else

```java
double balance = 5000000;
double withdrawAmount = 3000000;

// if đơn giản
if (withdrawAmount > balance) {
    System.out.println("Số dư không đủ!");
}

// if-else
if (withdrawAmount <= balance) {
    balance -= withdrawAmount;
    System.out.printf("Rút thành công. Số dư còn: %,.0f VND%n", balance);
} else {
    System.out.println("Số dư không đủ để thực hiện giao dịch.");
}

// if - else if - else (nhiều điều kiện)
double amount = 750000;

if (amount >= 1000000) {
    System.out.println("Khách hàng VIP - Giảm 10%");
} else if (amount >= 500000) {
    System.out.println("Khách hàng thân thiết - Giảm 5%");
} else if (amount >= 200000) {
    System.out.println("Giảm 2%");
} else {
    System.out.println("Không có khuyến mãi");
}
// Output: "Khách hàng thân thiết - Giảm 5%"
```

### 7.2 Ternary Operator (Toán tử 3 ngôi)

```java
// condition ? valueIfTrue : valueIfFalse
int balance = 5000000;
String status = (balance > 0) ? "Active" : "Inactive";

double discount = (balance >= 1000000) ? 0.10 : 0.05;

// Nested ternary (tránh dùng quá phức tạp - khó đọc)
String tier = (balance >= 10000000) ? "Gold"
            : (balance >= 5000000) ? "Silver"
            : "Bronze";
```

### 7.3 switch / case

```java
int menuChoice = 2;

switch (menuChoice) {
    case 1:
        System.out.println("Xem số dư");
        break;    // ⚠️ PHẢI có break, nếu không sẽ "fall through"
    case 2:
        System.out.println("Nạp tiền");
        break;
    case 3:
        System.out.println("Rút tiền");
        break;
    case 4:
        System.out.println("Chuyển khoản");
        break;
    default:
        System.out.println("Lựa chọn không hợp lệ");
}

// Switch với String (Java 7+)
String transactionType = "TRANSFER";
switch (transactionType) {
    case "DEPOSIT":
        System.out.println("Xử lý nạp tiền");
        break;
    case "WITHDRAW":
        System.out.println("Xử lý rút tiền");
        break;
    case "TRANSFER":
        System.out.println("Xử lý chuyển khoản");
        break;
}

// Enhanced switch (Java 14+) - không cần break
String message = switch (transactionType) {
    case "DEPOSIT" -> "Nạp tiền thành công";
    case "WITHDRAW" -> "Rút tiền thành công";
    case "TRANSFER" -> "Chuyển khoản thành công";
    default -> "Loại giao dịch không xác định";
};
```

---

## 8. VÒNG LẶP CƠ BẢN (Giới thiệu)

> Chi tiết sẽ học ở Ngày 2. Hôm nay chỉ cần biết `while` cho ATM Console.

### 8.1 while loop

```java
// Cú pháp: while (điều_kiện) { ... }

// Ví dụ: Menu ATM chạy cho đến khi user chọn thoát
boolean running = true;
while (running) {
    System.out.println("1. Xem số dư");
    System.out.println("2. Thoát");

    int choice = scanner.nextInt();
    if (choice == 2) {
        running = false;  // Thoát vòng lặp
    }
}
```

---

## 9. BÀI TẬP THỰC HÀNH

### Bài 1: Hello Banking (Dễ)
Viết chương trình in ra thông tin cá nhân:
```
=== THÔNG TIN KHÁCH HÀNG ===
Họ tên:    Nguyen Van A
Tuổi:      28
Tài khoản: 0123456789
Số dư:     15,750,000 VND
Trạng thái: Active
```

### Bài 2: Máy tính lãi suất (Trung bình)
→ Xem file `SimpleInterestCalculator.java`

### Bài 3: Đổi tiền (Trung bình)
- Nhập số tiền VND
- Hiển thị tương đương USD, EUR, JPY
- Tỷ giá: 1 USD = 24,500 VND, 1 EUR = 26,700 VND, 1 JPY = 164 VND

### Bài 4: Xếp loại tài khoản (Trung bình)
- Nhập số dư tài khoản
- Phân loại:
  - >= 1 tỷ: "Platinum"
  - >= 500 triệu: "Gold"
  - >= 100 triệu: "Silver"
  - >= 10 triệu: "Bronze"
  - < 10 triệu: "Standard"

### Bài 5: ATM Console (Nâng cao)
→ Xem file `ATMConsole.java`

---

## 10. TÓM TẮT NGÀY 1

```
┌──────────────────────────────────────────┐
│            NGÀY 1 - TÓM TẮT             │
├──────────────────────────────────────────┤
│ ✓ Java = Platform independent, Banking   │
│ ✓ JDK > JRE > JVM                       │
│ ✓ Mọi code phải trong class             │
│ ✓ main() = điểm bắt đầu                 │
│ ✓ 8 primitive types + String             │
│ ✓ Operators: +,-,*,/,%, ==,!=, &&,||    │
│ ✓ Scanner cho input                      │
│ ✓ printf cho formatted output            │
│ ✓ if/else, switch cho điều kiện          │
│ ✓ while cho vòng lặp cơ bản             │
│                                          │
│ ⚠️ String dùng .equals() không dùng ==   │
│ ⚠️ Scanner: nextLine() sau nextInt()     │
│ ⚠️ Chia int/int = int (mất thập phân)   │
└──────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP (Quiz cho ngày mai)

1. JDK khác JRE khác JVM như thế nào?
2. `int x = 10/3;` → x bằng bao nhiêu? Tại sao?
3. Tại sao `"abc" == "abc"` có thể cho kết quả sai? Phải dùng gì thay thế?
4. `final` keyword dùng để làm gì?
5. Sự khác nhau giữa `System.out.println` và `System.out.printf`?
6. Tại sao cần `scanner.nextLine()` sau `scanner.nextInt()`?
