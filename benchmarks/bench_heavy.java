public class bench_heavy {
    public static void main(String[] args) {
        long sum = 0;
        for (int i = 0; i < 100000000; i++) {
            sum = sum + i;
        }
        System.out.println(sum);
    }
}
