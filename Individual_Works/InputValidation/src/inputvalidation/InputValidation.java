package inputvalidation;
import java.util.Scanner;
public class InputValidation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        do {
            System.out.print("Введіть число від 10 до 100: ");
            n = scanner.nextInt();
        } while (n < 10 || n > 100);
        System.out.println("Значення прийнято");
        scanner.close();
    }
}
