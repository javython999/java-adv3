package lambda.lambda5.map;

import java.util.ArrayList;
import java.util.List;

public class MapMainV1 {

    public static void main(String[] args) {

        List<String> list = List.of("1", "12", "123", "1234");

        List<Integer> numbers = mapStringToInteger(list);
        System.out.println("numbers = " + numbers);

        List<Integer> lengths = mapStringToLength(list);
        System.out.println("lengths = " + lengths);
    }

    private static List<Integer> mapStringToInteger(List<String> list) {
        List<Integer> numbers = new ArrayList<>();
        for (String element : list) {
            numbers.add(Integer.valueOf(element));
        }
        return numbers;
    }

    private static List<Integer> mapStringToLength(List<String> list) {
        List<Integer> numbers = new ArrayList<>();
        for (String element : list) {
            numbers.add(element.length());
        }
        return numbers;
    }
}
