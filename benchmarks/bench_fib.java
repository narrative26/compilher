public class bench_fib {
    static int fib(int a, int b, int n) {
        if (n == 1) return b;
        return fib(b, a + b, n - 1);
    }

    public static void main(String[] args) {
        int result = 0;
        for (int i = 0; i < 10000; i++) {
            result = fib(0, 1, 30);
        }
        System.out.println(result);
    }
}
