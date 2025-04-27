package lambda.ex3;

import java.util.List;
import java.util.function.BinaryOperator;

public class ReduceExample {

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4);

        int sum = reduce(list, 0, (result, element) -> result + element);
        System.out.println("합(누적 +): " + sum);

        int multiply = reduce(list, 1, (result, element) -> result * element);
        System.out.println("곱(누적 *): " + multiply);

    }

    private static int reduce(List<Integer> list, int initial, BinaryOperator<Integer> reducer) {
        int result = initial;
        for (Integer element : list) {
            result = reducer.apply(result, element);
        }
        return result;
    }
}
