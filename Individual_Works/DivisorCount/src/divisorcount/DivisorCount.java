package divisorcount;
import java.util.Scanner;
public class DivisorCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть натуральне число: ");
        int n = scanner.nextInt();
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                count++;
            }
        }
        System.out.println("Кількість дільників: " + count);
        scanner.close();
    }
}
