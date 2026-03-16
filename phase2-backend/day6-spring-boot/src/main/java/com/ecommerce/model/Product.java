package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * Ngày 6 - Model class cho Product
 *
 * Hôm nay chưa dùng JPA/Database, chỉ dùng ArrayList để lưu tạm.
 * Ngày 7 sẽ thêm @Entity và kết nối database.
 */
public class Product {
    private Long id;
    private String name;
    private double price;
    private String category;
    private int quantity;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;

    // === Constructors ===

    public Product() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public Product(Long id, String name, double price, String category, int quantity) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    // === Getters & Setters ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
