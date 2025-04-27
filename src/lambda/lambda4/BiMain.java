package lambda.lambda4;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BiMain {

    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("add = " + add.apply(5, 10));

        BiConsumer<String, Integer> repeat = (a, b) -> {
            for (int i = 0; i < b; i++) {
                System.out.print(a);
            }
            System.out.println();
        };
        repeat.accept("hello", 5);

        BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
        System.out.println("isGreater = " + isGreater.test(10, 5));
    }
}
