package absolutevalue;
import java.util.Scanner;
public class AbsoluteValue {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть число: ");
        double x = scanner.nextDouble();
        double result;
        if (x >= 0) {
            result = x;
        } else {
            result = -x;
        }
        System.out.println("Модуль числа: " + result);
        scanner.close();
    }
}
