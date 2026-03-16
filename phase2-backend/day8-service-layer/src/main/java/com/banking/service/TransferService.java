package com.banking.service;

import com.banking.dto.TransferRequest;
import com.banking.dto.TransferResponse;
import com.banking.model.*;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Ngày 8 - Service chuyển khoản với đầy đủ business logic
 *
 * Business Rules:
 * 1. Không chuyển cho chính mình
 * 2. Cả 2 tài khoản phải active
 * 3. Sender phải đủ số dư (bao gồm phí)
 * 4. Giới hạn 500M VND/ngày
 * 5. Phí: nội bộ miễn phí, liên ngân hàng 0.05% (min 10K, max 50K)
 */
@Service
public class TransferService {

    private static final BigDecimal DAILY_LIMIT = new BigDecimal("500000000");
    private static final BigDecimal FEE_RATE = new BigDecimal("0.0005");
    private static final BigDecimal MIN_FEE = new BigDecimal("10000");
    private static final BigDecimal MAX_FEE = new BigDecimal("50000");

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository,
                           TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        // Rule 1: Không chuyển cho chính mình
        if (request.getFromAccount().equals(request.getToAccount())) {
            throw new IllegalArgumentException("Không thể chuyển khoản cho chính mình");
        }

        // Tìm tài khoản
        Account sender = findAccountOrThrow(request.getFromAccount());
        Account receiver = findAccountOrThrow(request.getToAccount());

        // Rule 2: Cả 2 phải active
        validateActiveAccount(sender);
        validateActiveAccount(receiver);

        // Rule 5: Tính phí
        BigDecimal fee = calculateTransferFee(request.getAmount());

        // Rule 3: Kiểm tra số dư (amount + fee)
        BigDecimal totalDebit = request.getAmount().add(fee);
        if (sender.getBalance().compareTo(totalDebit) < 0) {
            throw new IllegalStateException(
                    String.format("Số dư không đủ. Cần: %s VND (bao gồm phí: %s). Hiện có: %s VND",
                            totalDebit.toPlainString(), fee.toPlainString(),
                            sender.getBalance().toPlainString()));
        }

        // Rule 4: Giới hạn ngày
        validateDailyLimit(sender.getId(), request.getAmount());

        // === Thực hiện chuyển khoản ===
        sender.setBalance(sender.getBalance().subtract(totalDebit));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Ghi giao dịch
        String desc = request.getDescription() != null ? request.getDescription() : "Chuyển khoản";

        Transaction senderTx = new Transaction(
                sender, TransactionType.TRANSFER_OUT,
                request.getAmount().negate(), sender.getBalance(),
                desc + " → " + receiver.getAccountNumber());
        senderTx.setRelatedAccount(receiver.getAccountNumber());
        senderTx.setFee(fee);

        Transaction receiverTx = new Transaction(
                receiver, TransactionType.TRANSFER_IN,
                request.getAmount(), receiver.getBalance(),
                desc + " ← " + sender.getAccountNumber());
        receiverTx.setRelatedAccount(sender.getAccountNumber());

        transactionRepository.saveAll(List.of(senderTx, receiverTx));

        // Trả response
        return TransferResponse.success(
                senderTx.getId().toString(),
                request.getAmount(),
                fee,
                sender.getBalance(),
                sender.getAccountNumber(),
                receiver.getAccountNumber());
    }

    // === Private helper methods ===

    private Account findAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy tài khoản: " + accountNumber));
    }

    private void validateActiveAccount(Account account) {
        if (!account.isActive()) {
            throw new IllegalStateException(
                    "Tài khoản " + account.getAccountNumber() + " đã bị khóa");
        }
    }

    private BigDecimal calculateTransferFee(BigDecimal amount) {
        BigDecimal fee = amount.multiply(FEE_RATE).setScale(0, RoundingMode.CEILING);
        if (fee.compareTo(MIN_FEE) < 0) return MIN_FEE;
        if (fee.compareTo(MAX_FEE) > 0) return MAX_FEE;
        return fee;
    }

    private void validateDailyLimit(Long accountId, BigDecimal amount) {
        BigDecimal todayTotal = transactionRepository.sumTodayTransfers(
                accountId, LocalDate.now().atStartOfDay());
        if (todayTotal == null) todayTotal = BigDecimal.ZERO;

        if (todayTotal.add(amount).compareTo(DAILY_LIMIT) > 0) {
            BigDecimal remaining = DAILY_LIMIT.subtract(todayTotal);
            throw new IllegalStateException(
                    String.format("Vượt hạn mức chuyển khoản trong ngày. Còn lại: %s VND",
                            remaining.toPlainString()));
        }
    }
}
