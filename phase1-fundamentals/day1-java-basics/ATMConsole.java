import java.util.Scanner;

/**
 * Ngày 1 - Mini Project: ATM Console đơn giản
 *
 * Yêu cầu:
 * 1. Hiển thị menu: Xem số dư, Nạp tiền, Rút tiền, Thoát
 * 2. Xử lý input từ người dùng
 * 3. Kiểm tra điều kiện rút tiền (không rút quá số dư)
 * 4. Vòng lặp cho đến khi chọn Thoát
 *
 * Kiến thức: Scanner, if/else, while loop, format output
 */
public class ATMConsole {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double balance = 1000000; // Số dư ban đầu: 1,000,000 VND
        boolean running = true;

        System.out.println("╔═══════════════════════════════╗");
        System.out.println("║     CHÀO MỪNG ĐẾN VỚI ATM    ║");
        System.out.println("╚═══════════════════════════════╝");

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Xem số dư");
            System.out.println("2. Nạp tiền");
            System.out.println("3. Rút tiền");
            System.out.println("4. Thoát");
            System.out.print("\nChọn chức năng (1-4): ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // TODO: Hiển thị số dư hiện tại
                    // Gợi ý: System.out.printf("Số dư: %,.0f VND%n", balance);
                    break;

                case 2:
                    // TODO: Nạp tiền
                    // 1. Hỏi số tiền muốn nạp
                    // 2. Validate: số tiền > 0
                    // 3. Cộng vào balance
                    // 4. Hiển thị thông báo thành công
                    break;

                case 3:
                    // TODO: Rút tiền
                    // 1. Hỏi số tiền muốn rút
                    // 2. Validate: số tiền > 0
                    // 3. Validate: số tiền <= balance
                    // 4. Trừ khỏi balance
                    // 5. Hiển thị thông báo thành công hoặc lỗi
                    break;

                case 4:
                    running = false;
                    System.out.println("\nCảm ơn bạn đã sử dụng ATM. Tạm biệt!");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn 1-4.");
            }
        }

        scanner.close();
    }
}
