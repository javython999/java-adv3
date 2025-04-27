package lambda.ex3;


import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class MapExample {

    public static void main(String[] args) {
        List<String> list = List.of("hello", "java", "lambda");

        List<String> upperCaseList = map(list, str -> str.toUpperCase());
        System.out.println("대문자 변환 결과: " + upperCaseList);

        List<String> decoratedList = map(list, str -> "***".concat(str).concat("***"));
        System.out.println("특수문자 데코 결과:" + decoratedList);

    }

    private static List<String> map(List<String> list, UnaryOperator<String> function) {
        List<String> mappedList = new ArrayList<>();
        for (String element : list) {
            mappedList.add(function.apply(element));
        }
        return mappedList;
    }
}
