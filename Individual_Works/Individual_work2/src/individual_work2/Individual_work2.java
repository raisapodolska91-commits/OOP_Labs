package individual_work2;

public class Individual_work2 {

    public static void main(String[] args) {

        int n = 100;
        int operations = 0;

        for (int i = 0; i < n; i += 5) {
            int j = n;

            while (j > 1) {
                operations++;
                j /= 4;
            }
        }

        System.out.println("n = " + n);
        System.out.println("Кількість операцій: " + operations);
    }
}
