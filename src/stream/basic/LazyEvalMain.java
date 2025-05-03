package stream.basic;

import lambda.lambda5.mystream.MyStreamV3;

import java.util.List;

public class LazyEvalMain {

    public static void main(String[] args) {
        List<Integer> data = List.of(1, 2, 3, 4, 5, 6);
        
        ex1(data);
        ex2(data);
    }

    private static void ex1(List<Integer> data) {
        System.out.println("== MyStreamV3 시작 ==");
        List<Integer> result = MyStreamV3.of(data)
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                })
                .toList();
        System.out.println(result);
        System.out.println("== MyStreamV3 종료 ==");
    }

    private static void ex2(List<Integer> data) {
        System.out.println("== Stream API 시작 ==");
        List<Integer> result = data.stream()
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                })
                .toList();
        System.out.println(result);
        System.out.println("== Stream API 종료 ==");
    }
}
