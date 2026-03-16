import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Ngày 2 - Mini Project: Quản lý giỏ hàng (Shopping Cart)
 *
 * Yêu cầu:
 * 1. Thêm sản phẩm (tên, giá, số lượng)
 * 2. Xóa sản phẩm khỏi giỏ
 * 3. Hiển thị giỏ hàng dạng bảng
 * 4. Tính tổng tiền
 * 5. Áp dụng giảm giá theo tier (>500K giảm 5%, >1M giảm 10%)
 *
 * Kiến thức: ArrayList, HashMap, for-each, String formatting
 */
public class ShoppingCart {

    // Lưu trữ giỏ hàng: tên sản phẩm -> [giá, số lượng]
    static ArrayList<String> productNames = new ArrayList<>();
    static ArrayList<Double> productPrices = new ArrayList<>();
    static ArrayList<Integer> productQuantities = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== GIỎ HÀNG CỦA BẠN ===");
            System.out.println("1. Thêm sản phẩm");
            System.out.println("2. Xóa sản phẩm");
            System.out.println("3. Xem giỏ hàng");
            System.out.println("4. Thanh toán");
            System.out.println("5. Thoát");
            System.out.print("Chọn: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    removeProduct(scanner);
                    break;
                case 3:
                    displayCart();
                    break;
                case 4:
                    checkout();
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
        scanner.close();
    }

    static void addProduct(Scanner scanner) {
        // TODO: Implement
        // 1. Hỏi tên sản phẩm
        // 2. Hỏi giá
        // 3. Hỏi số lượng
        // 4. Thêm vào các ArrayList
        System.out.println("TODO: Implement addProduct");
    }

    static void removeProduct(Scanner scanner) {
        // TODO: Implement
        // 1. Hiển thị danh sách sản phẩm (đánh số)
        // 2. Hỏi muốn xóa sản phẩm nào
        // 3. Xóa khỏi các ArrayList
        System.out.println("TODO: Implement removeProduct");
    }

    static void displayCart() {
        // TODO: Implement
        // Hiển thị dạng bảng:
        // ┌────┬──────────────┬──────────┬──────┬──────────────┐
        // │ #  │ Tên SP       │ Đơn giá  │ SL   │ Thành tiền   │
        // ├────┼──────────────┼──────────┼──────┼──────────────┤
        // │ 1  │ iPhone 15    │ 25,990K  │ 1    │ 25,990,000   │
        // └────┴──────────────┴──────────┴──────┴──────────────┘
        System.out.println("TODO: Implement displayCart");
    }

    static void checkout() {
        // TODO: Implement
        // 1. Tính tổng tiền
        // 2. Áp dụng giảm giá:
        //    - Tổng > 1,000,000 → giảm 10%
        //    - Tổng > 500,000 → giảm 5%
        //    - Còn lại → không giảm
        // 3. Hiển thị hóa đơn
        System.out.println("TODO: Implement checkout");
    }

    static double calculateTotal() {
        double total = 0;
        for (int i = 0; i < productNames.size(); i++) {
            total += productPrices.get(i) * productQuantities.get(i);
        }
        return total;
    }

    static double calculateDiscount(double total) {
        // TODO: Implement discount logic
        // >1M → 10%, >500K → 5%, else → 0%
        return 0;
    }
}
