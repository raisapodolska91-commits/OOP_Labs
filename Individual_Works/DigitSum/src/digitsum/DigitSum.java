package digitsum;
import java.util.Scanner;
public class DigitSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть додатне ціле число: ");
        int n = scanner.nextInt();
        int sum = 0;
        while (n > 0) {
            int digit = n % 10;
            sum += digit;
            n /= 10;
        }
        System.out.println("Сума цифр: " + sum);
        scanner.close();
    }
}
