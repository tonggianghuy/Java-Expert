package com.banking;

import com.banking.model.*;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Ngày 7 - Seed data khi khởi động
 *
 * CommandLineRunner: chạy 1 lần sau khi Spring Boot khởi động xong.
 * Kiểm tra nếu DB trống → tạo dữ liệu mẫu.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DataSeeder(AccountRepository accountRepository,
                      TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        if (accountRepository.count() > 0) {
            System.out.println("Database already has data. Skipping seed.");
            return;
        }

        System.out.println("Seeding database...");

        // Tạo tài khoản mẫu
        Account acc1 = new Account("VCB0001001", "Nguyen Van A", AccountType.SAVINGS);
        acc1.setBalance(new BigDecimal("15000000"));
        accountRepository.save(acc1);

        Account acc2 = new Account("VCB0001002", "Tran Thi B", AccountType.CHECKING);
        acc2.setBalance(new BigDecimal("8500000"));
        accountRepository.save(acc2);

        Account acc3 = new Account("VCB0001003", "Le Van C", AccountType.SAVINGS);
        acc3.setBalance(new BigDecimal("42000000"));
        accountRepository.save(acc3);

        // Tạo giao dịch mẫu cho acc1
        transactionRepository.save(new Transaction(
                acc1, TransactionType.DEPOSIT, new BigDecimal("20000000"),
                new BigDecimal("20000000"), "Nạp tiền lần đầu"));

        transactionRepository.save(new Transaction(
                acc1, TransactionType.WITHDRAW, new BigDecimal("-3000000"),
                new BigDecimal("17000000"), "Rút tiền ATM"));

        transactionRepository.save(new Transaction(
                acc1, TransactionType.TRANSFER_OUT, new BigDecimal("-2000000"),
                new BigDecimal("15000000"), "Chuyển khoản đến VCB0001002"));

        // Giao dịch cho acc2
        transactionRepository.save(new Transaction(
                acc2, TransactionType.DEPOSIT, new BigDecimal("10000000"),
                new BigDecimal("10000000"), "Nạp tiền"));

        transactionRepository.save(new Transaction(
                acc2, TransactionType.TRANSFER_IN, new BigDecimal("2000000"),
                new BigDecimal("12000000"), "Nhận từ VCB0001001"));

        System.out.println("Seeded 3 accounts and 5 transactions.");
    }
}
