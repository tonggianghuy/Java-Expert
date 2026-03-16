import java.util.Scanner;

/**
 * Ngày 1 - Bài tập: Máy tính lãi suất đơn giản
 *
 * Yêu cầu:
 * - Nhập số tiền gửi, lãi suất/năm (%), số tháng gửi
 * - Tính tiền lãi = Tiền gốc × Lãi suất × Thời gian
 * - In ra: tiền lãi, tổng tiền nhận được
 *
 * Kiến thức: Scanner, biến, kiểu dữ liệu, phép tính, format output
 */
public class SimpleInterestCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== MÁY TÍNH LÃI SUẤT ĐƠN GIẢN ===\n");

        System.out.print("Nhập số tiền gửi (VND): ");
        double principal = scanner.nextDouble();

        System.out.print("Nhập lãi suất năm (%): ");
        double annualRate = scanner.nextDouble();

        System.out.print("Nhập số tháng gửi: ");
        int months = scanner.nextInt();

        // TODO: Tính tiền lãi
        // Công thức: interest = principal * (annualRate / 100) * (months / 12.0)
        double interest = 0; // <-- Thay thế bằng công thức đúng

        // TODO: Tính tổng tiền nhận được
        double total = 0; // <-- Thay thế bằng công thức đúng

        System.out.println("\n=== KẾT QUẢ ===");
        System.out.printf("Tiền gốc:     %,.0f VND%n", principal);
        System.out.printf("Lãi suất:     %.2f%%/năm%n", annualRate);
        System.out.printf("Thời gian:    %d tháng%n", months);
        System.out.printf("Tiền lãi:     %,.0f VND%n", interest);
        System.out.printf("Tổng nhận:    %,.0f VND%n", total);

        scanner.close();
    }
}
