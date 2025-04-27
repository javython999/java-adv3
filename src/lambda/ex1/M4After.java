package lambda.ex1;

import lambda.Procedure;

import java.util.Arrays;

public class M4After {

    public static void main(String[] args) {

        Procedure procedure1 = () -> {
            int N = 100;
            long sum = 0;
            for (int i = 1; i <= N; i++) {
                sum += i;
            }
            System.out.println("[1 부터" + N + "까지 합] = " + sum);
        };

        Procedure procedure2 = () -> {
            int[] arr = { 4, 3, 2, 1 };
            System.out.println("원본 배열: " + Arrays.toString(arr));
            Arrays.sort(arr);
            System.out.println("배열 정렬: " + Arrays.toString(arr));
        };

        measure(procedure1);
        System.out.println();
        measure(procedure2);
    }

    private static void measure(Procedure procedure) {
        long startNs = System.nanoTime();
        procedure.run();
        long endNs = System.nanoTime();
        System.out.println("실행 시간: " + (endNs - startNs) + "ns");
    }
}
