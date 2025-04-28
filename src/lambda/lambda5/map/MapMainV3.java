package lambda.lambda5.map;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MapMainV3 {

    public static void main(String[] args) {

        List<String> list = List.of("1", "12", "123", "1234");

        List<Integer> toNumber = map(list, s -> Integer.valueOf(s));
        System.out.println("numbers = " + toNumber);

        List<Integer> toLength = map(list, s -> s.length());
        System.out.println("lengths = " + toLength);
    }

    private static List<Integer> map(List<String> list, Function<String, Integer> mapper) {
        List<Integer> numbers = new ArrayList<>();
        for (String element : list) {
            numbers.add(mapper.apply(element));
        }
        return numbers;
    }

}
