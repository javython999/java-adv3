package stream;

import java.util.List;

public class StreamStartMain {

    public static void main(String[] args) {
        List<String> names = List.of("Apple", "Banana", "Berry", "Tomato");

        // 'B'로 시작하는 이름만 필터 후 대문자로 바꿔서 리스트 수집
        List<String> upperNames1 = names.stream()
                .filter(name -> name.startsWith("B"))
                .map(s -> s.toUpperCase())
                .toList();
        System.out.println(upperNames1);


        names.stream()
                .filter(name -> name.startsWith("B"))
                .map(String::toUpperCase)
                .forEach(System.out::println);

        List<String> upperNames2 = names.stream()
                .filter(name -> name.startsWith("B"))
                .map(String::toUpperCase)
                .toList();
        System.out.println(upperNames2);
    }
}
