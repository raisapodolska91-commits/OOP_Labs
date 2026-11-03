package deliverycost;
import java.util.Scanner;
public class DeliveryCost {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть масу посилки: ");
        double weight = scanner.nextDouble();
        if (weight <= 0) {
            System.out.println("Помилкова маса");
        } else {
            int cost;
            if (weight <= 2) {
                cost = 80;
            } else if (weight <= 5) {
                cost = 120;
            } else if (weight <= 10) {
                cost = 180;
            } else {
                cost = 300;
            }
            System.out.println("Вартість доставки: " + cost + " грн");
        }
        scanner.close();
    }
}
