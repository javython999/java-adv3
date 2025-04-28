package lambda.lambda5.map;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MapMainV2 {

    public static void main(String[] args) {

        List<String> list = List.of("1", "12", "123", "1234");

        Function<String, Integer> mapper1 = s -> Integer.valueOf(s);
        List<Integer> toNumber = map(list, mapper1);
        System.out.println("numbers = " + toNumber);

        Function<String, Integer> mapper2 = s -> s.length();
        List<Integer> toLength = map(list, mapper2);
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
