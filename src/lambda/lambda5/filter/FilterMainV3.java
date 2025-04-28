package lambda.lambda5.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilterMainV3 {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 짝수만 거르기
        List<Integer> evenNumbers = filter(numbers, x -> x % 2 == 0);
        System.out.println("evenNumbers = " + evenNumbers);

        // 홀수만 거르기
        List<Integer> oddNumbers = filter(numbers, x -> x % 2 == 1);
        System.out.println("oddNumbers = " + oddNumbers);

    }

    private static List<Integer> filter(List<Integer> numbers, Predicate<Integer> predicate) {
        List<Integer> filteredNumber = new ArrayList<>();
        for (Integer number : numbers) {
            if (predicate.test(number)) {
                filteredNumber.add(number);
            }
        }
        return filteredNumber;
    }
}
