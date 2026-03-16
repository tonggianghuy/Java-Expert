package com.banking.repository;

import com.banking.model.Account;
import com.banking.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Ngày 7 - Repository cho Account
 *
 * Kế thừa JpaRepository → tự động có: save, findById, findAll, deleteById, count...
 * Thêm custom methods bằng cách đặt tên theo convention hoặc @Query
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    // === Derived Queries (Spring tự tạo SQL từ tên method) ===

    // SELECT * FROM accounts WHERE account_number = ?
    Optional<Account> findByAccountNumber(String accountNumber);

    // SELECT * FROM accounts WHERE owner_name LIKE '%name%' (case-insensitive)
    List<Account> findByOwnerNameContainingIgnoreCase(String name);

    // SELECT * FROM accounts WHERE account_type = ?
    List<Account> findByAccountType(AccountType type);

    // SELECT * FROM accounts WHERE is_active = true
    List<Account> findByActiveTrue();

    // SELECT * FROM accounts WHERE account_type = ? AND is_active = true
    List<Account> findByAccountTypeAndActiveTrue(AccountType type);

    // SELECT * FROM accounts WHERE balance > ? ORDER BY balance DESC
    List<Account> findByBalanceGreaterThanOrderByBalanceDesc(BigDecimal minBalance);

    // SELECT * FROM accounts WHERE balance BETWEEN ? AND ?
    List<Account> findByBalanceBetween(BigDecimal min, BigDecimal max);

    // SELECT COUNT(*) FROM accounts WHERE account_type = ?
    long countByAccountType(AccountType type);

    // SELECT EXISTS(SELECT 1 FROM accounts WHERE account_number = ?)
    boolean existsByAccountNumber(String accountNumber);

    // Top 10 tài khoản có số dư cao nhất
    List<Account> findTop10ByOrderByBalanceDesc();

    // === Custom Queries (@Query) ===

    // JPQL: dùng tên Entity/field (Account, balance, active...)
    @Query("SELECT a FROM Account a WHERE a.balance > :minBalance AND a.active = true")
    List<Account> findWealthyActiveAccounts(@Param("minBalance") BigDecimal minBalance);

    // JPQL: Tính tổng số dư theo loại tài khoản
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.accountType = :type")
    BigDecimal sumBalanceByType(@Param("type") AccountType type);

    // TODO: Thêm các custom queries khác
    // Ví dụ: Tìm accounts chưa có giao dịch nào
    // Ví dụ: Tìm accounts có giao dịch lớn trong ngày
}
