package lambda.ex1;

import lambda.MyFunction;

public class M5After {

    public static void main(String[] args) {
        System.out.println(getOperation("add").apply(1, 2));
        System.out.println(getOperation("sub").apply(1, 2));
        System.out.println(getOperation("xxx").apply(1, 2));

    }

    private static MyFunction getOperation(String operator) {
        switch (operator) {
            case "add":
                return (int a, int b) -> a + b;
            case "sub":
                return (int a, int b) -> a - b;
            default:
                return (int a, int b) -> 0;
        }
    }
}
