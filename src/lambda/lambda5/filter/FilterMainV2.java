package lambda.lambda5.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilterMainV2 {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 짝수만 거르기
        Predicate<Integer> isEven = number -> number % 2 == 0;
        List<Integer> evenNumbers = filter(numbers,  isEven);
        System.out.println("evenNumbers = " + evenNumbers);

        // 홀수만 거르기
        Predicate<Integer> isOdd = x -> x % 2 == 0;
        List<Integer> oddNumbers = filter(numbers, isOdd);
        System.out.println("oddNumbers = " + oddNumbers);

    }

    private static List<Integer> filter(List<Integer> numbers, Predicate<Integer> predicate) {
        List<Integer> filteredNumber = new ArrayList<>();
        for (Integer number : numbers) {
            boolean testResult = predicate.test(number);
            if (testResult) {
                filteredNumber.add(number);
            }
        }
        return filteredNumber;
    }
}
