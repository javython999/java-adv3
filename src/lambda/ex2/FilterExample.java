package lambda.ex2;

import java.util.ArrayList;
import java.util.List;

public class FilterExample {

    public static void main(String[] args) {
        List<Integer> list = List.of(-3, -2, -1, 1, 2, 3, 5);

        List<Integer> negative = filter(list, element -> element < 0);
        System.out.println("음수만: " + negative);

        List<Integer> even = filter(list, element -> element % 2 == 0);
        System.out.println("짝수만: " + even);

    }

    private static List<Integer> filter(List<Integer> list, MyPredicate predicate) {
        List<Integer> filteredList = new ArrayList<>();
        for (Integer element : list) {
            if (predicate.test(element)) {
                filteredList.add(element);
            }
        }
        return filteredList;
    }
}
