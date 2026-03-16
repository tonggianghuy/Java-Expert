package com.banking.service;

import com.banking.dto.TransferRequest;
import com.banking.dto.TransferResponse;
import com.banking.model.*;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Ngày 14 - Unit Tests cho TransferService
 *
 * Pattern: Given → When → Then
 * Naming: methodName_ShouldExpectedBehavior_WhenCondition
 */
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
        sender = new Account("VCB001", "Nguyen Van A", AccountType.SAVINGS);
        sender.setId(1L);
        sender.setBalance(new BigDecimal("10000000")); // 10M

        receiver = new Account("VCB002", "Tran Thi B", AccountType.CHECKING);
        receiver.setId(2L);
        receiver.setBalance(new BigDecimal("5000000")); // 5M
    }

    @Test
    @DisplayName("Chuyển khoản thành công khi đủ điều kiện")
    void transfer_ShouldSucceed_WhenValidRequest() {
        // Given
        TransferRequest request = new TransferRequest("VCB001", "VCB002", new BigDecimal("3000000"));
        when(accountRepository.findByAccountNumber("VCB001")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002")).thenReturn(Optional.of(receiver));
        when(transactionRepository.sumTodayTransfers(anyLong(), any())).thenReturn(BigDecimal.ZERO);

        // When
        TransferResponse response = transferService.transfer(request);

        // Then
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(response.getAmount()).isEqualByComparingTo("3000000");
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Thất bại khi số dư không đủ")
    void transfer_ShouldThrow_WhenInsufficientFunds() {
        // Given
        TransferRequest request = new TransferRequest("VCB001", "VCB002", new BigDecimal("20000000"));
        when(accountRepository.findByAccountNumber("VCB001")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002")).thenReturn(Optional.of(receiver));

        // When & Then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Số dư không đủ");

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Thất bại khi chuyển cho chính mình")
    void transfer_ShouldThrow_WhenSameAccount() {
        // Given
        TransferRequest request = new TransferRequest("VCB001", "VCB001", new BigDecimal("1000000"));

        // When & Then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("chính mình");
    }

    @Test
    @DisplayName("Thất bại khi tài khoản bị freeze")
    void transfer_ShouldThrow_WhenAccountFrozen() {
        // Given
        sender.setActive(false);
        TransferRequest request = new TransferRequest("VCB001", "VCB002", new BigDecimal("1000000"));
        when(accountRepository.findByAccountNumber("VCB001")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002")).thenReturn(Optional.of(receiver));

        // When & Then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("khóa");
    }

    @Test
    @DisplayName("Thất bại khi vượt hạn mức ngày")
    void transfer_ShouldThrow_WhenDailyLimitExceeded() {
        // Given
        TransferRequest request = new TransferRequest("VCB001", "VCB002", new BigDecimal("5000000"));
        when(accountRepository.findByAccountNumber("VCB001")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB002")).thenReturn(Optional.of(receiver));
        when(transactionRepository.sumTodayTransfers(anyLong(), any()))
                .thenReturn(new BigDecimal("498000000")); // Đã gần 500M

        // When & Then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("hạn mức");
    }

    @Test
    @DisplayName("Thất bại khi tài khoản nhận không tồn tại")
    void transfer_ShouldThrow_WhenReceiverNotFound() {
        // Given
        TransferRequest request = new TransferRequest("VCB001", "VCB999", new BigDecimal("1000000"));
        when(accountRepository.findByAccountNumber("VCB001")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("VCB999")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("VCB999");
    }
}
