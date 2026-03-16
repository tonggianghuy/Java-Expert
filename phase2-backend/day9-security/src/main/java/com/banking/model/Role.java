package com.banking.model;

/**
 * Ngày 9 - Roles cho RBAC
 *
 * USER:   Khách hàng - xem/thao tác tài khoản của mình
 * TELLER: Giao dịch viên - tạo tài khoản, hỗ trợ khách hàng
 * ADMIN:  Quản trị - quản lý users, freeze/unfreeze, xem thống kê
 */
public enum Role {
    USER,
    TELLER,
    ADMIN
}
