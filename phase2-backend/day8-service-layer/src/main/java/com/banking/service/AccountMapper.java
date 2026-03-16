package com.banking.service;

import com.banking.dto.AccountResponse;
import com.banking.model.Account;

import java.util.List;

/**
 * Ngày 8 - Mapper: Entity ↔ DTO
 *
 * Chuyển đổi giữa Account entity và AccountResponse DTO.
 * Đảm bảo không leak sensitive fields ra API.
 */
public class AccountMapper {

    private AccountMapper() {} // Utility class, không tạo instance

    public static AccountResponse toResponse(Account account) {
        AccountResponse dto = new AccountResponse();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setOwnerName(account.getOwnerName());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType().name());
        dto.setActive(account.isActive());
        dto.setCreatedAt(account.getCreatedAt());
        return dto;
    }

    public static List<AccountResponse> toResponseList(List<Account> accounts) {
        return accounts.stream()
                .map(AccountMapper::toResponse)
                .toList();
    }
}
