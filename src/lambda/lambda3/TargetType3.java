package lambda.lambda3;

import java.util.function.Function;

public class TargetType3 {

    public static void main(String[] args) {
        Function<Integer, String> functionA = i -> "value = " + i;
        System.out.println(functionA.apply(10));

        Function<Integer, String> functionB = functionA;
        System.out.println(functionB.apply(20));
    }
}
