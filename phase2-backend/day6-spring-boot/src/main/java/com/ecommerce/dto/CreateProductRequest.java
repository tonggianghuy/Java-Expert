package com.ecommerce.dto;

import jakarta.validation.constraints.*;

/**
 * Ngày 6 - DTO cho tạo sản phẩm
 *
 * Validation annotations kiểm tra input TRƯỚC KHI vào Controller.
 * Nếu fail → tự động trả 400 Bad Request.
 */
public class CreateProductRequest {

    @NotBlank(message = "Tên sản phẩm không được trống")
    @Size(min = 2, max = 100, message = "Tên sản phẩm từ 2-100 ký tự")
    private String name;

    @NotNull(message = "Giá không được null")
    @Min(value = 0, message = "Giá phải >= 0")
    @Max(value = 999999999, message = "Giá không vượt quá 999,999,999")
    private Double price;

    @NotBlank(message = "Danh mục không được trống")
    private String category;

    @Min(value = 0, message = "Số lượng phải >= 0")
    private int quantity;

    private String description;

    // === Getters & Setters ===

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
