package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CreateProductRequest;
import com.ecommerce.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ngày 6 - Mini Project: E-Commerce Product API
 *
 * Endpoints:
 *   GET    /api/products              → Lấy tất cả (hỗ trợ search, filter, pagination)
 *   GET    /api/products/{id}         → Lấy theo ID
 *   POST   /api/products              → Tạo mới
 *   PUT    /api/products/{id}         → Cập nhật
 *   DELETE /api/products/{id}         → Xóa
 *   GET    /api/products/search       → Tìm kiếm nâng cao
 *
 * Test với Postman:
 *   1. POST http://localhost:8080/api/products
 *      Body: { "name": "iPhone 15", "price": 25990000, "category": "Electronics", "quantity": 50 }
 *   2. GET  http://localhost:8080/api/products
 *   3. GET  http://localhost:8080/api/products/1
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Tạm dùng ArrayList (Ngày 7 sẽ chuyển sang Database)
    private final List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    // Seed data khi khởi tạo
    public ProductController() {
        products.add(new Product(nextId++, "iPhone 15 Pro", 28990000, "Electronics", 50));
        products.add(new Product(nextId++, "MacBook Air M2", 27990000, "Electronics", 30));
        products.add(new Product(nextId++, "Áo thun Uniqlo", 399000, "Clothing", 200));
        products.add(new Product(nextId++, "Sách Clean Code", 350000, "Books", 100));
        products.add(new Product(nextId++, "Tai nghe AirPods Pro", 5990000, "Electronics", 80));
    }

    // ============================================================
    // GET /api/products - Lấy tất cả sản phẩm (hỗ trợ pagination)
    // ============================================================
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int start = page * size;
        int end = Math.min(start + size, products.size());

        if (start >= products.size()) {
            return ResponseEntity.ok(ApiResponse.success(List.of(),
                    "Trang " + page + " không có dữ liệu"));
        }

        List<Product> pageData = products.subList(start, end);
        return ResponseEntity.ok(ApiResponse.success(pageData,
                String.format("Trang %d/%d, Tổng: %d sản phẩm",
                        page, (int) Math.ceil((double) products.size() / size),
                        products.size())));
    }

    // ============================================================
    // GET /api/products/{id} - Lấy sản phẩm theo ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(p -> ResponseEntity.ok(ApiResponse.success(p)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Không tìm thấy sản phẩm với ID: " + id)));
    }

    // ============================================================
    // POST /api/products - Tạo sản phẩm mới
    // ============================================================
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {

        Product product = new Product();
        product.setId(nextId++);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());

        products.add(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(product, "Tạo sản phẩm thành công"));
    }

    // ============================================================
    // PUT /api/products/{id} - Cập nhật sản phẩm
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request) {

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                Product product = products.get(i);
                product.setName(request.getName());
                product.setPrice(request.getPrice());
                product.setCategory(request.getCategory());
                product.setQuantity(request.getQuantity());
                product.setDescription(request.getDescription());
                return ResponseEntity.ok(ApiResponse.success(product, "Cập nhật thành công"));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Không tìm thấy sản phẩm với ID: " + id));
    }

    // ============================================================
    // DELETE /api/products/{id} - Xóa sản phẩm
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        boolean removed = products.removeIf(p -> p.getId().equals(id));

        if (removed) {
            return ResponseEntity.ok(ApiResponse.success(null, "Xóa sản phẩm thành công"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Không tìm thấy sản phẩm với ID: " + id));
    }

    // ============================================================
    // GET /api/products/search?name=iphone&category=Electronics&minPrice=1000000&maxPrice=50000000
    // ============================================================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "999999999") double maxPrice) {

        List<Product> results = products.stream()
                .filter(p -> p.isActive())
                .filter(p -> name == null ||
                        p.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> category == null ||
                        p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(results,
                String.format("Tìm thấy %d sản phẩm", results.size())));
    }
}
