package com.banking.repository;

import com.banking.model.Transaction;
import com.banking.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Ngày 7 - Repository cho Transaction
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Giao dịch của 1 account, sắp xếp mới nhất trước
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);

    // Giao dịch có phân trang
    Page<Transaction> findByAccountId(Long accountId, Pageable pageable);

    // Giao dịch theo loại
    List<Transaction> findByAccountIdAndType(Long accountId, TransactionType type);

    // Giao dịch trong khoảng thời gian
    List<Transaction> findByAccountIdAndCreatedAtBetween(
            Long accountId, LocalDateTime start, LocalDateTime end);

    // Tổng chuyển khoản trong ngày (cho daily limit)
    @Query("SELECT COALESCE(SUM(ABS(t.amount)), 0) FROM Transaction t " +
           "WHERE t.account.id = :accountId " +
           "AND t.type = 'TRANSFER_OUT' " +
           "AND t.createdAt >= :startOfDay")
    BigDecimal sumTodayTransfers(
            @Param("accountId") Long accountId,
            @Param("startOfDay") LocalDateTime startOfDay);

    // Đếm giao dịch trong ngày
    @Query("SELECT COUNT(t) FROM Transaction t " +
           "WHERE t.account.id = :accountId " +
           "AND t.createdAt >= :startOfDay")
    long countTodayTransactions(
            @Param("accountId") Long accountId,
            @Param("startOfDay") LocalDateTime startOfDay);
}
