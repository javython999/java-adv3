package lambda.lambda4;

import java.util.function.IntFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

public class PrimitiveFunction {

    public static void main(String[] args) {
        // 기본형 매개변수, IntFunction, LongFunction, DoubleFunction
        IntFunction<String> intFunction = i -> "value = " + i;
        System.out.println("intFunction.apply(10) = " + intFunction.apply(10));

        // 기본형 반환 ToIntFunction, ToLongFunction, ToDoubleFunction
        ToIntFunction<String> toIntFunction = s -> s.length();
        System.out.println("toIntFunction.applyAsInt(\"hello\") = " + toIntFunction.applyAsInt("hello"));

        // 기본형 매개변수, 기본형 반환
        IntToLongFunction intToLongFunction = i -> i * 100L;
        System.out.println("intToLongFunction.applyAsLong(10) = " + intToLongFunction.applyAsLong(10));

        // IntUnaryOperator
        IntUnaryOperator intUnaryOperator = i -> i * 100;
        System.out.println("intUnaryOperator.applyAsInt(10) = " + intUnaryOperator.applyAsInt(10));
    }
}
