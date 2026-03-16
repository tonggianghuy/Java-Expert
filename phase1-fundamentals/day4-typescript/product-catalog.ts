/**
 * Ngày 4 - Mini Project: Product Catalog CLI (TypeScript)
 *
 * Yêu cầu:
 * 1. CRUD sản phẩm với type safety
 * 2. Tìm kiếm, lọc theo category/price range
 * 3. Export danh sách ra JSON
 * 4. Sử dụng interface, generic, enum, union type
 *
 * Kiến thức: TypeScript types, interfaces, generics, enum, async
 */

// === Types & Interfaces ===

enum Category {
  ELECTRONICS = 'electronics',
  CLOTHING = 'clothing',
  FOOD = 'food',
  BOOKS = 'books',
}

interface Product {
  id: number;
  name: string;
  price: number;
  category: Category;
  inStock: boolean;
  quantity: number;
  description?: string; // Optional field
}

interface SearchFilter {
  keyword?: string;
  category?: Category;
  minPrice?: number;
  maxPrice?: number;
  inStockOnly?: boolean;
}

// Response type - dùng Generic
interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
  timestamp: Date;
}

// === Product Catalog Class ===

class ProductCatalog {
  private products: Product[] = [];
  private nextId: number = 1;

  // Create
  addProduct(input: Omit<Product, 'id'>): ApiResponse<Product> {
    const product: Product = {
      id: this.nextId++,
      ...input,
    };
    this.products.push(product);

    return {
      success: true,
      data: product,
      message: `Thêm sản phẩm "${product.name}" thành công`,
      timestamp: new Date(),
    };
  }

  // Read - tất cả
  getAllProducts(): Product[] {
    return [...this.products]; // Return copy, không cho modify trực tiếp
  }

  // Read - theo ID
  getProductById(id: number): Product | undefined {
    return this.products.find(p => p.id === id);
  }

  // Update
  updateProduct(id: number, updates: Partial<Omit<Product, 'id'>>): ApiResponse<Product | null> {
    // TODO: Implement
    // 1. Tìm product theo id
    // 2. Nếu không tìm thấy, return error response
    // 3. Update các fields được truyền vào
    // 4. Return success response
    return {
      success: false,
      data: null,
      message: 'TODO: Implement updateProduct',
      timestamp: new Date(),
    };
  }

  // Delete
  deleteProduct(id: number): ApiResponse<null> {
    // TODO: Implement
    return {
      success: false,
      data: null,
      message: 'TODO: Implement deleteProduct',
      timestamp: new Date(),
    };
  }

  // Search & Filter
  searchProducts(filter: SearchFilter): Product[] {
    // TODO: Implement
    // Lọc products theo tất cả điều kiện trong filter
    // Nếu filter.keyword → tìm trong name (case-insensitive)
    // Nếu filter.category → lọc theo category
    // Nếu filter.minPrice → price >= minPrice
    // Nếu filter.maxPrice → price <= maxPrice
    // Nếu filter.inStockOnly → chỉ lấy inStock = true
    return [];
  }

  // Pagination - Generic helper
  paginate<T>(items: T[], page: number, pageSize: number): { data: T[]; total: number; totalPages: number } {
    const start = page * pageSize;
    const paginatedItems = items.slice(start, start + pageSize);

    return {
      data: paginatedItems,
      total: items.length,
      totalPages: Math.ceil(items.length / pageSize),
    };
  }

  // Export to JSON
  exportToJson(): string {
    return JSON.stringify(this.products, null, 2);
  }

  // Statistics
  getStatistics(): {
    totalProducts: number;
    byCategory: Record<Category, number>;
    averagePrice: number;
    outOfStock: number;
  } {
    // TODO: Implement
    // Thống kê sản phẩm theo category, giá trung bình, hết hàng
    return {
      totalProducts: 0,
      byCategory: {} as Record<Category, number>,
      averagePrice: 0,
      outOfStock: 0,
    };
  }
}

// === Demo & Test ===

function main(): void {
  const catalog = new ProductCatalog();

  // Thêm sản phẩm mẫu
  catalog.addProduct({
    name: 'iPhone 15 Pro',
    price: 28990000,
    category: Category.ELECTRONICS,
    inStock: true,
    quantity: 50,
    description: 'Smartphone cao cấp từ Apple',
  });

  catalog.addProduct({
    name: 'Áo thun Uniqlo',
    price: 399000,
    category: Category.CLOTHING,
    inStock: true,
    quantity: 200,
  });

  catalog.addProduct({
    name: 'Clean Code - Robert C. Martin',
    price: 350000,
    category: Category.BOOKS,
    inStock: false,
    quantity: 0,
  });

  // Tìm kiếm
  console.log('\n=== TẤT CẢ SẢN PHẨM ===');
  console.log(catalog.getAllProducts());

  console.log('\n=== TÌM KIẾM: Electronics, giá > 1M ===');
  const results = catalog.searchProducts({
    category: Category.ELECTRONICS,
    minPrice: 1000000,
  });
  console.log(results);

  console.log('\n=== PAGINATION: Page 0, Size 2 ===');
  const page = catalog.paginate(catalog.getAllProducts(), 0, 2);
  console.log(page);

  console.log('\n=== EXPORT JSON ===');
  console.log(catalog.exportToJson());
}

main();
