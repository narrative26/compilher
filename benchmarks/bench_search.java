public class bench_search {
    static int binarySearch(int[] arr, int x, int n) {
        int low = 0, high = n - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] == x) return mid;
            else if (arr[mid] < x) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = new int[5];
        for (int i = 0; i < 5; i++) arr[i] = i + 1;
        int result = 0;
        for (int k = 0; k < 100000; k++) {
            result = binarySearch(arr, 3, 5);
        }
        System.out.println(result);
    }
}
