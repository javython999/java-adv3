package lambda.lambda5.map;

import java.util.List;

public class MapMainV5 {

    public static void main(String[] args) {

        List<String> list = List.of("apple", "banana", "orange");

        // String -> String
        List<String> toUpperCase = GenericMapper.map(list, s -> s.toUpperCase());
        System.out.println("toUpperCase = " + toUpperCase);

        // String -> Integer
        List<Integer> length = GenericMapper.map(list, s -> s.length());
        System.out.println("length = " + length);

        List<Integer> integers = List.of(1, 2, 3);
        List<String> starList = GenericMapper.map(integers, n -> "*".repeat(n));
        System.out.println("starList = " + starList);

    }

}
