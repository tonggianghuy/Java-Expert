import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Ngày 3 - Mini Project: Banking Account System
 *
 * Yêu cầu:
 * 1. Abstract class BankAccount với deposit(), withdraw(), calculateInterest()
 * 2. SavingsAccount - tính lãi theo tháng
 * 3. CheckingAccount - cho phép overdraft
 * 4. FixedDepositAccount - lãi suất cao, không rút trước hạn
 * 5. Chuyển tiền giữa các tài khoản
 * 6. Lịch sử giao dịch
 *
 * Kiến thức: OOP 4 pillars, abstract class, interface, polymorphism
 */

// === Transaction Record ===
class Transaction {
    private String type;        // DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
    private double amount;
    private double balanceAfter;
    private String description;
    private LocalDateTime timestamp;

    public Transaction(String type, double amount, double balanceAfter, String description) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] %-15s | %+,.0f VND | Số dư: %,.0f VND | %s",
                timestamp.toString().substring(0, 19), type, amount, balanceAfter, description);
    }
}

// === Abstract Base Class ===
abstract class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;
    private List<Transaction> transactions;

    public BankAccount(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // Encapsulation: protected getter cho subclass
    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }

    protected void setBalance(double balance) { this.balance = balance; }

    // Template method: deposit có thể override nếu cần
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0");
        }
        this.balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, this.balance, "Nạp tiền"));
        System.out.printf("Nạp thành công %,.0f VND. Số dư: %,.0f VND%n", amount, balance);
    }

    // Abstract method: mỗi loại account rút tiền khác nhau
    public abstract boolean withdraw(double amount);

    // Abstract method: mỗi loại account tính lãi khác nhau
    public abstract double calculateInterest();

    // Chuyển khoản
    public boolean transferTo(BankAccount receiver, double amount) {
        // TODO: Implement
        // 1. Gọi this.withdraw(amount)
        // 2. Nếu thành công, gọi receiver.deposit(amount)
        // 3. Ghi transaction cho cả 2 account
        // 4. Return true/false
        return false;
    }

    public void printStatement() {
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.printf("SÁO KÊ TÀI KHOẢN: %s - %s%n", accountNumber, ownerName);
        System.out.printf("Loại: %s%n", this.getClass().getSimpleName());
        System.out.println("═══════════════════════════════════════════════════");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.printf("%nSố dư hiện tại: %,.0f VND%n", balance);
        System.out.println("═══════════════════════════════════════════════════");
    }

    protected void addTransaction(Transaction t) {
        transactions.add(t);
    }
}

// === Savings Account ===
class SavingsAccount extends BankAccount {
    private double annualInterestRate; // Ví dụ: 0.055 = 5.5%/năm

    public SavingsAccount(String accountNumber, String ownerName, double initialBalance, double annualInterestRate) {
        super(accountNumber, ownerName, initialBalance);
        this.annualInterestRate = annualInterestRate;
    }

    @Override
    public boolean withdraw(double amount) {
        // TODO: Implement
        // 1. Validate amount > 0
        // 2. Kiểm tra balance >= amount
        // 3. Trừ balance
        // 4. Ghi transaction
        // 5. Return true nếu thành công
        return false;
    }

    @Override
    public double calculateInterest() {
        // TODO: Tính lãi hàng tháng
        // interest = balance * annualInterestRate / 12
        return 0;
    }
}

// === Checking Account ===
class CheckingAccount extends BankAccount {
    private double overdraftLimit; // Hạn mức thấu chi

    public CheckingAccount(String accountNumber, String ownerName, double initialBalance, double overdraftLimit) {
        super(accountNumber, ownerName, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        // TODO: Implement
        // Cho phép rút quá số dư nhưng trong hạn mức overdraft
        // Ví dụ: balance = 1M, overdraft = 500K → rút tối đa 1.5M
        return false;
    }

    @Override
    public double calculateInterest() {
        // Checking account không có lãi
        return 0;
    }
}

// === Fixed Deposit Account ===
class FixedDepositAccount extends BankAccount {
    // TODO: Implement
    // - Có term (số tháng gửi)
    // - Lãi suất cao hơn Savings
    // - KHÔNG cho phép rút trước hạn (withdraw luôn return false)
    // - calculateInterest() tính lãi theo term

    public FixedDepositAccount(String accountNumber, String ownerName, double initialBalance) {
        super(accountNumber, ownerName, initialBalance);
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.println("Tài khoản tiết kiệm có kỳ hạn không cho phép rút trước hạn!");
        return false;
    }

    @Override
    public double calculateInterest() {
        return 0;
    }
}

// === Main - Test everything ===
public class BankAccountSystem {
    public static void main(String[] args) {
        // Tạo các tài khoản
        SavingsAccount savings = new SavingsAccount("SAV001", "Nguyen Van A", 10000000, 0.055);
        CheckingAccount checking = new CheckingAccount("CHK001", "Nguyen Van A", 5000000, 2000000);

        // Test deposit
        savings.deposit(5000000);

        // Test withdraw
        savings.withdraw(3000000);

        // Test transfer
        savings.transferTo(checking, 2000000);

        // Test overdraft
        checking.withdraw(8000000); // Rút quá balance nhưng trong overdraft limit

        // In statement
        savings.printStatement();
        checking.printStatement();

        // Tính lãi
        System.out.printf("%nLãi tháng này của Savings: %,.0f VND%n", savings.calculateInterest());
    }
}
