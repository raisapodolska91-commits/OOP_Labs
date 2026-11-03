import java.util.Scanner;
public class PrimeNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть натуральне число N > 1: ");
        int n = scanner.nextInt();
        boolean isPrime = true;
        if (n <= 1) {
            isPrime = false;
        } else {
            for (int i = 2; i < n; i++) {
                if (n % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }
        if (isPrime) {
            System.out.println("Число просте");
        } else {
            System.out.println("Число не просте");
        }
        scanner.close();
    }
}
