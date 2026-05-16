public class bench_factorial {
    static int factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }

    public static void main(String[] args) {
        int result = 0;
        for (int i = 0; i < 100000; i++) {
            result = factorial(12);
        }
        System.out.println(result);
    }
}
