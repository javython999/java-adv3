package functional;

import java.util.ArrayList;
import java.util.List;

public class DeclarativeMain {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 명령형: for문과 조건 검사로 처리
        List<Integer> result1 = new ArrayList<>();
        for (Integer number : numbers) {
            if (number % 2 == 0) {
                result1.add(number * number);
            }
        }
        System.out.println("Imperative result = " + result1);


        // 선언형: 스트림 API 처리
        List<Integer> result2 = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .toList();
        System.out.println("Declarative result = " + result2);
    }
}
