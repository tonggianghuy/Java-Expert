# NGÀY 4: TypeScript Fundamentals & Modern JavaScript

---

## 1. TYPESCRIPT LÀ GÌ?

### 1.1 JavaScript vs TypeScript

```
JavaScript                          TypeScript
─────────────                       ──────────
- Dynamic typing                    - Static typing
- Lỗi phát hiện khi CHẠY           - Lỗi phát hiện khi VIẾT CODE
- Không cần compile                 - Compile sang JavaScript
- Linh hoạt nhưng dễ lỗi           - Nghiêm ngặt nhưng an toàn
```

```javascript
// JavaScript: Không báo lỗi cho đến khi chạy
function calculateTotal(price, quantity) {
    return price * quantity;
}
calculateTotal("abc", 5);  // NaN - chỉ phát hiện khi chạy!
```

```typescript
// TypeScript: Báo lỗi ngay khi viết
function calculateTotal(price: number, quantity: number): number {
    return price * quantity;
}
calculateTotal("abc", 5);  // ❌ ERROR ngay lập tức: "abc" is not number
```

### 1.2 Tại sao Ecommerce & Banking cần TypeScript?

| Lý do | Giải thích |
|-------|-----------|
| **Type Safety** | Tránh lỗi runtime, đặc biệt quan trọng khi xử lý tiền |
| **IDE Support** | Autocomplete, refactoring, go-to-definition |
| **Documentation** | Types = documentation sống |
| **Scale** | Dễ maintain codebase lớn (100K+ lines) |
| **Team Work** | Interface rõ ràng giữa frontend/backend |

### 1.3 TypeScript compile flow

```
TypeScript (.ts)  →  tsc (compiler)  →  JavaScript (.js)  →  Node.js / Browser
```

---

## 2. CÀI ĐẶT MÔI TRƯỜNG

### 2.1 Cài Node.js & TypeScript

```bash
# 1. Cài Node.js (LTS) từ https://nodejs.org
node -v     # v18.x.x hoặc v20.x.x
npm -v      # 9.x.x

# 2. Cài TypeScript globally
npm install -g typescript
tsc -v      # Version 5.x.x

# 3. Cài ts-node (chạy TypeScript trực tiếp)
npm install -g ts-node
```

### 2.2 Tạo TypeScript project

```bash
mkdir my-ts-project
cd my-ts-project
npm init -y
npm install typescript @types/node --save-dev
npx tsc --init     # Tạo tsconfig.json
```

### 2.3 tsconfig.json (cấu hình quan trọng)

```json
{
  "compilerOptions": {
    "target": "ES2020",          // Compile thành JS version nào
    "module": "commonjs",        // Hệ thống module
    "strict": true,              // BẬT tất cả strict checks
    "outDir": "./dist",          // Folder output JS
    "rootDir": "./src",          // Folder input TS
    "esModuleInterop": true,
    "resolveJsonModule": true,
    "declaration": true          // Tạo .d.ts files
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "dist"]
}
```

### 2.4 Chạy TypeScript

```bash
# Cách 1: Compile rồi chạy
tsc src/main.ts           # → dist/main.js
node dist/main.js

# Cách 2: Chạy trực tiếp (development)
ts-node src/main.ts

# Cách 3: Watch mode (tự compile khi save)
tsc --watch
```

---

## 3. TYPE SYSTEM CƠ BẢN

### 3.1 Primitive Types

```typescript
// Khai báo biến với type annotation
let customerName: string = "Nguyen Van A";
let age: number = 28;
let isVIP: boolean = true;
let balance: number = 15_000_000;  // underscore cho dễ đọc (= 15000000)

// Type inference (tự suy luận - không cần ghi type)
let productName = "iPhone 15";     // TypeScript tự hiểu: string
let price = 25_990_000;            // TypeScript tự hiểu: number
let inStock = true;                // TypeScript tự hiểu: boolean

// ⚠️ Khi nào cần ghi type rõ ràng?
let amount: number;      // Khai báo không gán giá trị
amount = 500000;         // Gán sau

// const: giống final trong Java
const TAX_RATE = 0.08;
const BANK_NAME = "MY BANK";
// TAX_RATE = 0.10;  // ❌ ERROR: Cannot assign to const
```

### 3.2 Special Types

```typescript
// null & undefined
let middleName: string | null = null;       // Có thể null
let nickname: string | undefined = undefined; // Có thể undefined

// any: tắt type checking (TRÁNH dùng!)
let data: any = "hello";
data = 42;        // OK
data = true;      // OK - NGUY HIỂM: mất type safety

// unknown: an toàn hơn any
let input: unknown = "hello";
// input.toUpperCase();     // ❌ ERROR: phải check type trước
if (typeof input === "string") {
    input.toUpperCase();    // ✅ OK sau khi check
}

// void: function không trả về gì
function logTransaction(message: string): void {
    console.log(`[LOG] ${message}`);
}

// never: function không bao giờ return (throw error, infinite loop)
function throwError(message: string): never {
    throw new Error(message);
}
```

### 3.3 Array & Tuple

```typescript
// Array (2 cách viết)
let prices: number[] = [299000, 1500000, 450000];
let names: Array<string> = ["iPhone", "MacBook", "AirPods"];

// Array methods (giống JavaScript)
prices.push(89000);                    // Thêm cuối
prices.pop();                          // Xóa cuối
let sorted = prices.sort((a, b) => a - b);  // Sắp xếp
let expensive = prices.filter(p => p > 500000);  // Lọc
let total = prices.reduce((sum, p) => sum + p, 0);  // Tính tổng

// Tuple: array với số lượng và kiểu phần tử CỐ ĐỊNH
let transaction: [string, number, Date] = ["DEPOSIT", 5000000, new Date()];
let [type, amount, date] = transaction;  // Destructuring

// Readonly array
const rates: readonly number[] = [5.5, 6.0, 7.2];
// rates.push(8.0);  // ❌ ERROR: readonly
```

### 3.4 Enum

```typescript
// Numeric enum
enum TransactionType {
    DEPOSIT = 1,
    WITHDRAW = 2,
    TRANSFER = 3,
}

let txType: TransactionType = TransactionType.DEPOSIT;
console.log(txType);  // 1

// String enum (phổ biến hơn trong thực tế)
enum OrderStatus {
    PENDING = "PENDING",
    CONFIRMED = "CONFIRMED",
    SHIPPING = "SHIPPING",
    DELIVERED = "DELIVERED",
    CANCELLED = "CANCELLED",
}

let status: OrderStatus = OrderStatus.PENDING;
console.log(status);  // "PENDING"

// const enum (hiệu suất cao hơn - inline khi compile)
const enum PaymentMethod {
    CASH = "CASH",
    CARD = "CARD",
    EWALLET = "EWALLET",
}
```

---

## 4. INTERFACE & TYPE

### 4.1 Interface

```typescript
// Định nghĩa shape (hình dạng) của object
interface Customer {
    id: number;
    name: string;
    email: string;
    phone?: string;              // Optional (có thể không có)
    readonly createdAt: Date;    // Chỉ đọc, không sửa
}

// Sử dụng
const customer: Customer = {
    id: 1,
    name: "Nguyen Van A",
    email: "a@email.com",
    createdAt: new Date(),
    // phone không bắt buộc
};

// customer.createdAt = new Date();  // ❌ ERROR: readonly

// Interface cho function
interface PriceCalculator {
    (price: number, quantity: number, discount?: number): number;
}

const calculateTotal: PriceCalculator = (price, quantity, discount = 0) => {
    return price * quantity * (1 - discount / 100);
};
```

### 4.2 Type Alias

```typescript
// Type alias: đặt tên cho bất kỳ type nào
type AccountNumber = string;
type Money = number;
type TransactionId = string;

// Union type: HOẶC
type PaymentStatus = "pending" | "completed" | "failed" | "refunded";
type ID = string | number;

// Intersection type: VÀ
type Timestamped = { createdAt: Date; updatedAt: Date };
type SoftDeletable = { deletedAt: Date | null };

type BaseEntity = Timestamped & SoftDeletable;
// BaseEntity = { createdAt: Date; updatedAt: Date; deletedAt: Date | null }

// Type cho object (tương tự interface)
type Product = {
    id: number;
    name: string;
    price: Money;
    category: string;
};
```

### 4.3 Interface vs Type - Khi nào dùng gì?

| Tiêu chí | Interface | Type |
|----------|-----------|------|
| Extend/Merge | `extends` + auto merge | `&` intersection |
| Union types | ❌ Không hỗ trợ | ✅ `"a" \| "b"` |
| Primitives | ❌ | ✅ `type ID = string` |
| Khi nào dùng? | Object shapes, class contracts | Union, intersection, aliases |

```typescript
// Interface: mở rộng được (declaration merging)
interface User {
    name: string;
}
interface User {        // Tự động merge
    email: string;
}
// User = { name: string; email: string }

// Type: KHÔNG thể merge
type Product = { name: string };
// type Product = { price: number };  // ❌ ERROR: Duplicate identifier
```

**Khuyến nghị:** Dùng `interface` cho objects/classes, dùng `type` cho unions và aliases.

---

## 5. FUNCTIONS

### 5.1 Function declarations

```typescript
// Named function
function calculateInterest(principal: number, rate: number, months: number): number {
    return principal * rate * months / 12;
}

// Arrow function (phổ biến hơn trong TypeScript)
const calculateFee = (amount: number, feeRate: number = 0.001): number => {
    return amount * feeRate;
};

// Function với optional parameter
const greetCustomer = (name: string, title?: string): string => {
    return title ? `Xin chào ${title} ${name}` : `Xin chào ${name}`;
};

greetCustomer("Anh");          // "Xin chào Anh"
greetCustomer("Anh", "Anh");   // "Xin chào Anh Anh"

// Function với default parameter
const formatMoney = (amount: number, currency: string = "VND"): string => {
    return `${amount.toLocaleString()} ${currency}`;
};

formatMoney(5000000);          // "5,000,000 VND"
formatMoney(100, "USD");       // "100 USD"

// Rest parameters
const sum = (...numbers: number[]): number => {
    return numbers.reduce((total, n) => total + n, 0);
};
sum(1, 2, 3, 4, 5);  // 15
```

### 5.2 Function Overloads

```typescript
// Khai báo overload signatures
function processPayment(amount: number): string;
function processPayment(amount: number, cardNumber: string): string;
function processPayment(amount: number, cardNumber?: string): string {
    if (cardNumber) {
        return `Thanh toán thẻ ****${cardNumber.slice(-4)}: ${amount}`;
    }
    return `Thanh toán tiền mặt: ${amount}`;
}
```

---

## 6. GENERICS (Kiểu tổng quát)

### 6.1 Tại sao cần Generics?

```typescript
// Không có generic: phải viết nhiều hàm
function getFirstNumber(arr: number[]): number { return arr[0]; }
function getFirstString(arr: string[]): string { return arr[0]; }

// Với generic: 1 hàm cho tất cả
function getFirst<T>(arr: T[]): T {
    return arr[0];
}

getFirst<number>([1, 2, 3]);        // number
getFirst<string>(["a", "b", "c"]);  // string
getFirst([true, false]);            // TypeScript tự suy luận: boolean
```

### 6.2 Generic Interface & Type

```typescript
// API Response wrapper
interface ApiResponse<T> {
    success: boolean;
    data: T;
    message: string;
    timestamp: Date;
}

// Sử dụng với các types khác nhau
type ProductResponse = ApiResponse<Product>;
type CustomerListResponse = ApiResponse<Customer[]>;
type LoginResponse = ApiResponse<{ token: string; expiresIn: number }>;

// Ví dụ
const response: ApiResponse<Product> = {
    success: true,
    data: { id: 1, name: "iPhone", price: 25990000, category: "Electronics" },
    message: "OK",
    timestamp: new Date(),
};

// Pagination response
interface PaginatedResponse<T> {
    data: T[];
    total: number;
    page: number;
    pageSize: number;
    totalPages: number;
}

type ProductPage = PaginatedResponse<Product>;
```

### 6.3 Generic Constraints

```typescript
// T phải có property 'id'
interface HasId {
    id: number;
}

function findById<T extends HasId>(items: T[], id: number): T | undefined {
    return items.find(item => item.id === id);
}

// T phải có property 'price'
interface HasPrice {
    price: number;
}

function getExpensive<T extends HasPrice>(items: T[], minPrice: number): T[] {
    return items.filter(item => item.price >= minPrice);
}
```

---

## 7. ASYNC / AWAIT & PROMISES

### 7.1 Đồng bộ vs Bất đồng bộ

```
Đồng bộ (Synchronous):           Bất đồng bộ (Asynchronous):
[Task 1] ────→                    [Task 1] ────→
              [Task 2] ────→      [Task 2] ────→
                       [Task 3]   [Task 3] ────→
Total: T1 + T2 + T3              Total: max(T1, T2, T3)
```

### 7.2 Promise

```typescript
// Promise = "Lời hứa" sẽ có kết quả trong tương lai
// 3 states: pending → fulfilled (resolved) | rejected

// Tạo Promise
const fetchBalance = (accountId: string): Promise<number> => {
    return new Promise((resolve, reject) => {
        // Giả lập gọi API (mất 1 giây)
        setTimeout(() => {
            if (accountId === "001") {
                resolve(5000000);  // Thành công
            } else {
                reject(new Error("Account not found"));  // Thất bại
            }
        }, 1000);
    });
};

// Sử dụng Promise với .then/.catch
fetchBalance("001")
    .then(balance => console.log(`Số dư: ${balance}`))
    .catch(error => console.error(`Lỗi: ${error.message}`));
```

### 7.3 Async / Await (Cách viết hiện đại - KHUYÊN DÙNG)

```typescript
// async function: luôn trả về Promise
// await: đợi Promise hoàn thành

async function getAccountInfo(accountId: string): Promise<void> {
    try {
        console.log("Đang tải...");
        const balance = await fetchBalance(accountId);  // Đợi kết quả
        console.log(`Số dư: ${balance.toLocaleString()} VND`);
    } catch (error) {
        if (error instanceof Error) {
            console.error(`Lỗi: ${error.message}`);
        }
    }
}

// Chạy nhiều async song song
async function getDashboardData(): Promise<void> {
    // Chạy TUẦN TỰ (chậm)
    const balance = await fetchBalance("001");
    const transactions = await fetchTransactions("001");
    const profile = await fetchProfile("001");

    // Chạy SONG SONG (nhanh)
    const [balance2, transactions2, profile2] = await Promise.all([
        fetchBalance("001"),
        fetchTransactions("001"),
        fetchProfile("001"),
    ]);
}
```

### 7.4 So sánh Java vs TypeScript async

```
Java:                                   TypeScript:
CompletableFuture<Double>       ←→      Promise<number>
.thenApply()                    ←→      .then()
.exceptionally()                ←→      .catch()
ExecutorService                 ←→      Event Loop (single-threaded)
```

---

## 8. DESTRUCTURING & SPREAD

### 8.1 Object Destructuring

```typescript
interface Customer {
    name: string;
    email: string;
    phone: string;
    address: { city: string; district: string };
}

const customer: Customer = {
    name: "Nguyen Van A",
    email: "a@email.com",
    phone: "0901234567",
    address: { city: "Ho Chi Minh", district: "Quan 1" },
};

// Destructuring: "mở hộp" object
const { name, email, phone } = customer;
console.log(name);   // "Nguyen Van A"
console.log(email);  // "a@email.com"

// Đổi tên
const { name: customerName, email: customerEmail } = customer;

// Nested destructuring
const { address: { city, district } } = customer;

// Default value
const { phone: tel = "N/A" } = customer;

// Trong function parameter
function sendEmail({ name, email }: Customer): void {
    console.log(`Gửi email đến ${name} (${email})`);
}
```

### 8.2 Array Destructuring

```typescript
const transaction: [string, number, string] = ["TRANSFER", 5000000, "2024-01-15"];

const [type, amount, date] = transaction;

// Bỏ qua phần tử
const [, amountOnly] = transaction;  // Chỉ lấy amount

// Rest element
const [first, ...rest] = [1, 2, 3, 4, 5];
// first = 1, rest = [2, 3, 4, 5]
```

### 8.3 Spread Operator (...)

```typescript
// Copy object (shallow copy)
const original = { name: "A", balance: 5000000 };
const copy = { ...original };             // Copy
const updated = { ...original, balance: 6000000 };  // Copy + override

// Merge objects
const defaults = { currency: "VND", locale: "vi-VN" };
const userPrefs = { locale: "en-US" };
const settings = { ...defaults, ...userPrefs };
// { currency: "VND", locale: "en-US" }  (userPrefs override)

// Copy array
const prices = [100, 200, 300];
const newPrices = [...prices, 400];       // [100, 200, 300, 400]
const merged = [...prices, ...newPrices]; // Merge 2 arrays
```

---

## 9. UTILITY TYPES (Types có sẵn của TypeScript)

```typescript
interface Product {
    id: number;
    name: string;
    price: number;
    description: string;
    category: string;
}

// Partial<T>: tất cả fields thành optional
type UpdateProductDTO = Partial<Product>;
// = { id?: number; name?: string; price?: number; ... }

// Required<T>: tất cả fields thành required
type StrictProduct = Required<Product>;

// Pick<T, Keys>: chỉ lấy một số fields
type ProductSummary = Pick<Product, "id" | "name" | "price">;
// = { id: number; name: string; price: number }

// Omit<T, Keys>: bỏ một số fields
type CreateProductDTO = Omit<Product, "id">;
// = { name: string; price: number; description: string; category: string }

// Readonly<T>: tất cả fields thành readonly
type FrozenProduct = Readonly<Product>;

// Record<K, V>: tạo object type với key K và value V
type CategoryCount = Record<string, number>;
// = { [key: string]: number }
const stats: CategoryCount = {
    electronics: 150,
    clothing: 300,
    food: 80,
};
```

---

## 10. SO SÁNH JAVA VS TYPESCRIPT

| Khái niệm | Java | TypeScript |
|-----------|------|-----------|
| Khai báo biến | `int x = 5;` | `let x: number = 5;` |
| Hằng số | `final int X = 5;` | `const X = 5;` |
| Null safety | `Optional<String>` | `string \| null` |
| Array | `int[] arr = new int[5];` | `let arr: number[] = [];` |
| HashMap | `HashMap<String, Integer>` | `Map<string, number>` hoặc `Record<string, number>` |
| Interface | `interface Payable { }` | `interface Payable { }` |
| Class | `public class Product { }` | `class Product { }` |
| Lambda | `(x) -> x * 2` | `(x) => x * 2` |
| Async | `CompletableFuture` | `Promise` + `async/await` |
| Access modifier | `public/private/protected` | `public/private/protected` |
| Generics | `List<Product>` | `Product[]` hoặc `Array<Product>` |
| Print | `System.out.println()` | `console.log()` |

---

## 11. TÓM TẮT NGÀY 4

```
┌────────────────────────────────────────────────┐
│             NGÀY 4 - TÓM TẮT                  │
├────────────────────────────────────────────────┤
│ TypeScript = JavaScript + Type System          │
│                                                │
│ Types:                                         │
│ ✓ string, number, boolean, null, undefined     │
│ ✓ any (tránh), unknown (an toàn hơn)          │
│ ✓ Array<T>, Tuple, Enum                       │
│                                                │
│ Interface & Type:                              │
│ ✓ interface → object shapes, extendable        │
│ ✓ type → unions, intersections, aliases        │
│                                                │
│ Functions:                                     │
│ ✓ Arrow functions, optional/default params     │
│ ✓ Generics: function<T>(arg: T): T            │
│                                                │
│ Async:                                         │
│ ✓ Promise, async/await, try/catch             │
│ ✓ Promise.all() cho parallel                   │
│                                                │
│ Modern syntax:                                 │
│ ✓ Destructuring, Spread operator              │
│ ✓ Template literals: `Hello ${name}`          │
│ ✓ Utility types: Partial, Pick, Omit          │
│                                                │
│ ⚠️ Tránh dùng any                             │
│ ⚠️ Interface cho objects, Type cho unions      │
│ ⚠️ Luôn xử lý error trong async              │
└────────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. TypeScript khác JavaScript ở điểm nào quan trọng nhất?
2. `interface` và `type` khác nhau thế nào? Cho ví dụ khi nào dùng cái nào.
3. `any` và `unknown` khác nhau gì? Tại sao nên tránh `any`?
4. Generic là gì? Viết 1 generic function `filter<T>`.
5. `async/await` hoạt động thế nào? Khác gì `.then().catch()`?
6. `Partial<T>` và `Omit<T, K>` dùng khi nào?
