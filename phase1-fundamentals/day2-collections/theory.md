# NGÀY 2: Control Flow, Arrays & Collections

---

## 1. VÒNG LẶP (Loops)

### 1.1 for loop - Khi biết trước số lần lặp

```
Cú pháp:
for (khởi_tạo; điều_kiện; cập_nhật) {
    // code lặp lại
}
```

```java
// Ví dụ 1: In danh sách giao dịch
for (int i = 1; i <= 5; i++) {
    System.out.println("Giao dịch #" + i);
}
// Giao dịch #1
// Giao dịch #2
// ...
// Giao dịch #5

// Ví dụ 2: Tính tổng 1 → 100
int sum = 0;
for (int i = 1; i <= 100; i++) {
    sum += i;
}
System.out.println("Tổng 1-100 = " + sum);  // 5050

// Ví dụ 3: Đếm ngược
for (int i = 10; i >= 1; i--) {
    System.out.println(i + "...");
}
System.out.println("Hết thời gian giao dịch!");

// Ví dụ 4: Bước nhảy (step)
for (int i = 0; i <= 100; i += 10) {
    System.out.println(i + "%");  // 0%, 10%, 20%, ... 100%
}
```

**Luồng thực thi:**
```
for (int i = 0;  i < 3;  i++)
     ─────┬───  ──┬──  ──┬──
       (1)         (2)     (4)
                    │
              ┌─────┘
              ▼
         true → (3) thực thi body → quay lại (4) rồi (2)
         false → thoát vòng lặp
```

### 1.2 while loop - Khi không biết trước số lần lặp

```java
// Ví dụ: ATM - lặp cho đến khi nhập đúng PIN
int attempts = 0;
int maxAttempts = 3;
boolean authenticated = false;

while (attempts < maxAttempts && !authenticated) {
    System.out.print("Nhập PIN: ");
    String pin = scanner.nextLine();

    if (pin.equals("1234")) {
        authenticated = true;
        System.out.println("Đăng nhập thành công!");
    } else {
        attempts++;
        System.out.printf("Sai PIN. Còn %d lần thử.%n", maxAttempts - attempts);
    }
}

if (!authenticated) {
    System.out.println("Tài khoản bị khóa do nhập sai PIN 3 lần!");
}
```

### 1.3 do-while loop - Chạy ít nhất 1 lần

```java
// Khác while: kiểm tra điều kiện SAU KHI chạy body
int choice;
do {
    System.out.println("\n=== MENU ===");
    System.out.println("1. Xem số dư");
    System.out.println("2. Chuyển khoản");
    System.out.println("3. Thoát");
    System.out.print("Chọn: ");
    choice = scanner.nextInt();

    switch (choice) {
        case 1 -> System.out.println("Số dư: 5,000,000 VND");
        case 2 -> System.out.println("Đang chuyển khoản...");
        case 3 -> System.out.println("Tạm biệt!");
        default -> System.out.println("Không hợp lệ!");
    }
} while (choice != 3);  // Tiếp tục nếu chưa chọn thoát
```

**So sánh while vs do-while:**
```
while:                    do-while:
┌──→ Kiểm tra ──false──→ │    Thực thi body
│       │                 │         │
│     true                │    Kiểm tra ──false──→
│       │                 │         │
│    Thực thi body        └──true───┘
└───────┘

while có thể không chạy lần nào
do-while chạy ÍT NHẤT 1 lần
```

### 1.4 for-each loop - Duyệt collection/array

```java
String[] products = {"iPhone", "MacBook", "AirPods"};

// for thường
for (int i = 0; i < products.length; i++) {
    System.out.println(products[i]);
}

// for-each (gọn hơn, dễ đọc hơn)
for (String product : products) {
    System.out.println(product);
}
// Đọc: "với mỗi String product TRONG products"
```

### 1.5 break và continue

```java
// break: thoát vòng lặp ngay lập tức
for (int i = 1; i <= 100; i++) {
    if (i == 5) {
        break;  // Dừng khi i = 5
    }
    System.out.println(i);  // In: 1, 2, 3, 4
}

// continue: bỏ qua lần lặp hiện tại, nhảy sang lần tiếp
for (int i = 1; i <= 10; i++) {
    if (i % 3 == 0) {
        continue;  // Bỏ qua số chia hết cho 3
    }
    System.out.println(i);  // In: 1, 2, 4, 5, 7, 8, 10
}

// Ví dụ Banking: Tìm giao dịch đầu tiên > 1 triệu
double[] transactions = {200000, 500000, 1500000, 300000, 2000000};
for (double t : transactions) {
    if (t > 1000000) {
        System.out.printf("Giao dịch lớn: %,.0f VND%n", t);
        break;  // Chỉ cần tìm giao dịch đầu tiên
    }
}
```

### 1.6 Nested Loops (Vòng lặp lồng nhau)

```java
// In bảng nhân
for (int i = 1; i <= 9; i++) {
    for (int j = 1; j <= 9; j++) {
        System.out.printf("%4d", i * j);
    }
    System.out.println();
}

// Ví dụ Banking: In statement cho nhiều tài khoản
String[] accounts = {"001", "002", "003"};
double[][] monthlyBalances = {
    {10000000, 12000000, 11000000},  // account 001: tháng 1,2,3
    {5000000, 5500000, 6000000},     // account 002
    {20000000, 19000000, 21000000}   // account 003
};

for (int i = 0; i < accounts.length; i++) {
    System.out.printf("Tài khoản %s: ", accounts[i]);
    for (int j = 0; j < monthlyBalances[i].length; j++) {
        System.out.printf("T%d: %,.0f | ", j + 1, monthlyBalances[i][j]);
    }
    System.out.println();
}
```

---

## 2. ARRAYS (Mảng)

### 2.1 Array là gì?

Array là **container chứa nhiều giá trị cùng kiểu**, có kích thước **cố định** khi tạo.

```
Index:   [0]         [1]         [2]         [3]         [4]
Value: "iPhone"   "MacBook"   "AirPods"   "iPad"     "iMac"
         ↑
    Bắt đầu từ 0!
```

### 2.2 Khai báo và khởi tạo Array

```java
// Cách 1: Khai báo kích thước, gán sau
int[] prices = new int[5];       // Mảng 5 phần tử, mặc định = 0
prices[0] = 25990000;            // iPhone
prices[1] = 34990000;            // MacBook
prices[2] = 4990000;             // AirPods

// Cách 2: Khai báo + khởi tạo giá trị
String[] categories = {"Điện thoại", "Laptop", "Phụ kiện", "Tablet"};

double[] exchangeRates = {24500.0, 26700.0, 164.0};  // USD, EUR, JPY

// Cách 3: Mảng 2 chiều
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};
```

### 2.3 Truy cập và thao tác Array

```java
String[] accounts = {"VCB001", "VCB002", "VCB003"};

// Truy cập phần tử
String first = accounts[0];          // "VCB001"
String last = accounts[accounts.length - 1]; // "VCB003"

// Thay đổi giá trị
accounts[1] = "TCB002";

// Độ dài mảng
int size = accounts.length;  // 3 (KHÔNG có dấu () vì length là field, không phải method)

// ⚠️ ArrayIndexOutOfBoundsException
// accounts[5] = "XYZ";  // ❌ ERROR! Index 5 không tồn tại (chỉ có 0-2)

// Duyệt mảng
for (int i = 0; i < accounts.length; i++) {
    System.out.printf("[%d] %s%n", i, accounts[i]);
}

// Hoặc for-each
for (String acc : accounts) {
    System.out.println(acc);
}
```

### 2.4 Các thao tác phổ biến với Array

```java
import java.util.Arrays;

double[] prices = {299000, 1500000, 450000, 89000, 2100000};

// Sắp xếp
Arrays.sort(prices);  // [89000, 299000, 450000, 1500000, 2100000]

// Tìm kiếm (mảng phải được sort trước)
int index = Arrays.binarySearch(prices, 450000);  // 2

// Copy mảng
double[] copy = Arrays.copyOf(prices, prices.length);
double[] partial = Arrays.copyOfRange(prices, 1, 3); // [299000, 450000]

// So sánh 2 mảng
boolean same = Arrays.equals(prices, copy);  // true

// In mảng
System.out.println(Arrays.toString(prices));
// [89000.0, 299000.0, 450000.0, 1500000.0, 2100000.0]

// Fill mảng
int[] zeros = new int[5];
Arrays.fill(zeros, -1);  // [-1, -1, -1, -1, -1]
```

### 2.5 Hạn chế của Array

| Hạn chế | Giải pháp |
|---------|----------|
| Kích thước cố định | → Dùng `ArrayList` |
| Chỉ chứa 1 kiểu dữ liệu | → Dùng `Object[]` hoặc tạo class |
| Không có method tiện ích (add, remove) | → Dùng Collections |
| Không thể tìm kiếm dễ dàng theo key | → Dùng `HashMap` |

---

## 3. COLLECTIONS FRAMEWORK

### 3.1 Tổng quan

```
                    Collection (interface)
                    /         \
               List            Set
              /    \          /    \
        ArrayList  LinkedList  HashSet  TreeSet

                    Map (interface)
                   /           \
             HashMap        TreeMap
```

**Khi nào dùng gì?**

| Collection | Khi nào dùng | Ví dụ Banking/Ecommerce |
|-----------|-------------|------------------------|
| `ArrayList` | Danh sách có thứ tự, truy cập theo index | Danh sách giao dịch, sản phẩm |
| `LinkedList` | Thêm/xóa đầu/cuối nhiều | Queue xử lý giao dịch |
| `HashSet` | Không cho phép trùng lặp | Danh sách email khách hàng |
| `HashMap` | Tìm kiếm nhanh theo key | Tài khoản → số dư, mã SP → sản phẩm |
| `TreeMap` | HashMap nhưng key được sắp xếp | Báo cáo theo ngày |

### 3.2 ArrayList - Mảng động

```java
import java.util.ArrayList;
import java.util.Collections;

// Khai báo - phải dùng Wrapper type (Integer thay cho int)
ArrayList<String> customers = new ArrayList<>();

// Thêm phần tử
customers.add("Nguyen Van A");
customers.add("Tran Thi B");
customers.add("Le Van C");
customers.add(1, "Pham Van D");  // Chèn vào vị trí 1

// Truy cập
String first = customers.get(0);          // "Nguyen Van A"
int size = customers.size();               // 4

// Kiểm tra
boolean hasA = customers.contains("Nguyen Van A");  // true
int index = customers.indexOf("Le Van C");           // 3

// Xóa
customers.remove("Tran Thi B");      // Xóa theo giá trị
customers.remove(0);                  // Xóa theo index

// Thay đổi
customers.set(0, "Hoang Van E");

// Duyệt
for (String customer : customers) {
    System.out.println(customer);
}

// Sắp xếp
Collections.sort(customers);  // A-Z

// Xóa tất cả
customers.clear();

// Kiểm tra rỗng
boolean empty = customers.isEmpty();  // true
```

**Ví dụ thực tế: Quản lý giao dịch**

```java
ArrayList<Double> transactions = new ArrayList<>();
transactions.add(5000000.0);   // Nạp tiền
transactions.add(-2000000.0);  // Rút tiền
transactions.add(-500000.0);   // Rút tiền
transactions.add(10000000.0);  // Nạp tiền

// Tính tổng
double total = 0;
for (double t : transactions) {
    total += t;
}
System.out.printf("Tổng: %,.0f VND%n", total);  // 12,500,000 VND

// Đếm giao dịch rút tiền
int withdrawCount = 0;
for (double t : transactions) {
    if (t < 0) withdrawCount++;
}
System.out.println("Số lần rút: " + withdrawCount);  // 2

// Tìm giao dịch lớn nhất
double maxTransaction = Collections.max(transactions);
System.out.printf("GD lớn nhất: %,.0f VND%n", maxTransaction);
```

### 3.3 HashMap - Ánh xạ Key → Value

```java
import java.util.HashMap;
import java.util.Map;

// Khai báo: HashMap<KeyType, ValueType>
HashMap<String, Double> accountBalances = new HashMap<>();

// Thêm phần tử (put)
accountBalances.put("VCB001", 15000000.0);
accountBalances.put("VCB002", 8500000.0);
accountBalances.put("VCB003", 42000000.0);

// Truy cập (get)
double balance = accountBalances.get("VCB001");  // 15000000.0

// Truy cập an toàn (tránh NullPointerException)
double balance2 = accountBalances.getOrDefault("VCB999", 0.0);  // 0.0

// Kiểm tra key/value tồn tại
boolean exists = accountBalances.containsKey("VCB001");    // true
boolean hasRich = accountBalances.containsValue(42000000.0); // true

// Cập nhật
accountBalances.put("VCB001", 16000000.0);  // Ghi đè giá trị cũ

// Xóa
accountBalances.remove("VCB003");

// Kích thước
int count = accountBalances.size();  // 2

// Duyệt HashMap
for (Map.Entry<String, Double> entry : accountBalances.entrySet()) {
    System.out.printf("Tài khoản: %s | Số dư: %,.0f VND%n",
        entry.getKey(), entry.getValue());
}

// Chỉ duyệt keys
for (String key : accountBalances.keySet()) {
    System.out.println("Account: " + key);
}

// Chỉ duyệt values
for (Double value : accountBalances.values()) {
    System.out.printf("Balance: %,.0f%n", value);
}
```

**Ví dụ thực tế: Thống kê giao dịch theo loại**

```java
String[] txTypes = {"DEPOSIT", "WITHDRAW", "TRANSFER", "DEPOSIT", "WITHDRAW",
                    "DEPOSIT", "TRANSFER", "TRANSFER", "WITHDRAW", "DEPOSIT"};

HashMap<String, Integer> stats = new HashMap<>();

for (String type : txTypes) {
    // getOrDefault: nếu chưa có key thì trả về 0
    stats.put(type, stats.getOrDefault(type, 0) + 1);
}

System.out.println("=== THỐNG KÊ GIAO DỊCH ===");
for (Map.Entry<String, Integer> entry : stats.entrySet()) {
    System.out.printf("%-12s: %d lần%n", entry.getKey(), entry.getValue());
}
// DEPOSIT    : 4 lần
// WITHDRAW   : 3 lần
// TRANSFER   : 3 lần
```

### 3.4 HashSet - Tập hợp không trùng lặp

```java
import java.util.HashSet;

HashSet<String> uniqueCustomers = new HashSet<>();
uniqueCustomers.add("customer@email.com");
uniqueCustomers.add("user@email.com");
uniqueCustomers.add("customer@email.com");  // Không thêm (đã tồn tại)

System.out.println(uniqueCustomers.size());  // 2

// Kiểm tra
boolean exists = uniqueCustomers.contains("customer@email.com");  // true

// Use case: Tìm sản phẩm unique trong danh sách đơn hàng
String[] orderedProducts = {"iPhone", "MacBook", "iPhone", "AirPods", "MacBook"};
HashSet<String> uniqueProducts = new HashSet<>();
for (String p : orderedProducts) {
    uniqueProducts.add(p);
}
System.out.println("Sản phẩm unique: " + uniqueProducts);
// [iPhone, MacBook, AirPods]
```

---

## 4. STRING - Xử lý chuỗi

### 4.1 String là Immutable (Bất biến)

```java
String name = "Hello";
name.toUpperCase();          // Tạo String MỚI, KHÔNG thay đổi name
System.out.println(name);    // "Hello" (không đổi!)

name = name.toUpperCase();   // Phải gán lại
System.out.println(name);    // "HELLO"
```

### 4.2 Các method thường dùng

```java
String accountNumber = "  VCB-001-2024  ";

// Độ dài
int len = accountNumber.length();  // 18 (tính cả khoảng trắng)

// Cắt khoảng trắng đầu cuối
String trimmed = accountNumber.trim();  // "VCB-001-2024"

// Chuyển hoa/thường
String upper = trimmed.toUpperCase();  // "VCB-001-2024"
String lower = trimmed.toLowerCase();  // "vcb-001-2024"

// Lấy ký tự tại vị trí
char first = trimmed.charAt(0);  // 'V'

// Cắt chuỗi con
String bankCode = trimmed.substring(0, 3);     // "VCB"
String number = trimmed.substring(4, 7);       // "001"

// Tìm kiếm
boolean startsWith = trimmed.startsWith("VCB");  // true
boolean endsWith = trimmed.endsWith("2024");      // true
boolean contains = trimmed.contains("001");       // true
int index = trimmed.indexOf("-");                 // 3
int lastIndex = trimmed.lastIndexOf("-");         // 7

// Thay thế
String masked = trimmed.replace("001", "***");  // "VCB-***-2024"

// Tách chuỗi
String csv = "iPhone,25990000,50,Electronics";
String[] parts = csv.split(",");
// parts = ["iPhone", "25990000", "50", "Electronics"]

// Nối chuỗi
String joined = String.join(" | ", "VCB", "001", "Active");
// "VCB | 001 | Active"

// So sánh (LUÔN dùng equals, KHÔNG dùng ==)
String a = "Hello";
String b = "Hello";
boolean equal = a.equals(b);              // true
boolean equalIgnoreCase = a.equalsIgnoreCase("hello");  // true

// Kiểm tra rỗng
boolean isEmpty = "".isEmpty();       // true
boolean isBlank = "   ".isBlank();    // true (Java 11+)
```

### 4.3 StringBuilder - Nối chuỗi hiệu suất cao

```java
// ❌ Kém hiệu suất (tạo String mới mỗi lần nối)
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i + ",";  // Tạo 1000 String objects!
}

// ✅ Dùng StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(",");
}
String result2 = sb.toString();

// Ví dụ: Tạo bảng hiển thị
StringBuilder table = new StringBuilder();
table.append("┌────────────┬──────────────┐\n");
table.append("│ Tài khoản  │ Số dư        │\n");
table.append("├────────────┼──────────────┤\n");
table.append("│ VCB001     │ 15,000,000   │\n");
table.append("│ VCB002     │  8,500,000   │\n");
table.append("└────────────┴──────────────┘\n");
System.out.println(table);
```

---

## 5. WRAPPER CLASSES

### 5.1 Primitive vs Wrapper

Collections không thể chứa primitive types, phải dùng Wrapper:

| Primitive | Wrapper | Auto-boxing |
|-----------|---------|-------------|
| `int` | `Integer` | `Integer x = 5;` ← tự động wrap |
| `double` | `Double` | `Double d = 3.14;` |
| `boolean` | `Boolean` | `Boolean b = true;` |
| `char` | `Character` | `Character c = 'A';` |
| `long` | `Long` | `Long l = 100L;` |

```java
// Auto-boxing: primitive → Wrapper (tự động)
ArrayList<Integer> numbers = new ArrayList<>();
numbers.add(42);        // int 42 → Integer.valueOf(42) tự động
numbers.add(100);

// Auto-unboxing: Wrapper → primitive (tự động)
int first = numbers.get(0);  // Integer → int tự động

// Useful methods
int parsed = Integer.parseInt("12345");     // String → int
String str = Integer.toString(12345);        // int → String
int max = Integer.MAX_VALUE;                 // 2,147,483,647
int min = Integer.MIN_VALUE;                 // -2,147,483,648
```

---

## 6. BÀI TẬP THỰC HÀNH

### Bài 1: Tìm min/max giá sản phẩm (Dễ)
```java
double[] prices = {299000, 1500000, 450000, 89000, 2100000, 750000};
// Tìm giá cao nhất, thấp nhất, trung bình
// KHÔNG dùng Collections.max/min - tự viết bằng vòng lặp
```

### Bài 2: Đếm tần suất giao dịch (Trung bình)
```java
String[] types = {"DEPOSIT", "WITHDRAW", "TRANSFER", "DEPOSIT", "WITHDRAW",
                  "DEPOSIT", "TRANSFER", "TRANSFER", "WITHDRAW", "DEPOSIT"};
// Dùng HashMap đếm số lần xuất hiện mỗi loại
// In kết quả dạng bảng
```

### Bài 3: Sắp xếp sản phẩm theo giá (Trung bình)
```java
// Cho 2 ArrayList song song: tên SP và giá
// Sắp xếp theo giá tăng dần (phải giữ tên và giá khớp nhau)
```

### Bài 4: Mask số tài khoản (Trung bình)
```java
// Input:  "0123456789"
// Output: "******6789" (ẩn 6 số đầu)
// Áp dụng cho ArrayList<String> nhiều tài khoản
```

### Bài 5: Shopping Cart (Nâng cao)
→ Xem file `ShoppingCart.java`

---

## 7. TÓM TẮT NGÀY 2

```
┌──────────────────────────────────────────────┐
│              NGÀY 2 - TÓM TẮT               │
├──────────────────────────────────────────────┤
│ Loops:                                       │
│ ✓ for      → biết trước số lần              │
│ ✓ while    → không biết trước               │
│ ✓ do-while → chạy ít nhất 1 lần            │
│ ✓ for-each → duyệt collection              │
│ ✓ break (thoát) / continue (bỏ qua)        │
│                                              │
│ Arrays:                                      │
│ ✓ Kích thước cố định, truy cập bằng index  │
│ ✓ Arrays.sort(), Arrays.toString()           │
│                                              │
│ Collections:                                 │
│ ✓ ArrayList → danh sách động                │
│ ✓ HashMap   → key-value lookup nhanh        │
│ ✓ HashSet   → không trùng lặp              │
│                                              │
│ String:                                      │
│ ✓ Immutable (bất biến)                      │
│ ✓ equals(), split(), substring(), trim()    │
│ ✓ StringBuilder cho nối chuỗi nhiều         │
│                                              │
│ ⚠️ Array index bắt đầu từ 0                │
│ ⚠️ ArrayList dùng Wrapper (Integer, Double) │
│ ⚠️ HashMap.get() có thể trả null           │
└──────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. Sự khác nhau giữa `for`, `while`, `do-while`?
2. Array và ArrayList khác nhau thế nào? Khi nào dùng cái nào?
3. HashMap hoạt động như thế nào? Key có thể trùng không?
4. Tại sao String là immutable? Điều này ảnh hưởng gì khi nối chuỗi nhiều?
5. `ArrayList<int>` có hợp lệ không? Tại sao?
6. Làm sao để duyệt cả key và value của HashMap?
