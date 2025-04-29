package lambda.lambda5.mystream;

import java.util.List;

public class MyStreamV1Main {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> result1 = returnValue(numbers);
        System.out.println("result1 = " + result1);

        List<Integer> result2 = methodChain(numbers);
        System.out.println("result2 = " + result2);
    }

    private static List<Integer> methodChain(List<Integer> numbers) {
        MyStreamV1 stream = new MyStreamV1(numbers);
        MyStreamV1 result = stream.filter(n -> n % 2 == 0).map(n -> n * 2);
        return result.toList();
    }

    private static List<Integer> returnValue(List<Integer> numbers) {
        MyStreamV1 stream = new MyStreamV1(numbers);
        MyStreamV1 filteredStream = stream.filter(n -> n % 2 == 0);
        System.out.println("filteredStream = " + filteredStream.toList());

        MyStreamV1 mappedStream = filteredStream.map(n -> n * 2);
        System.out.println("mappedStream = " + mappedStream.toList());
        return mappedStream.toList();
    }
}
