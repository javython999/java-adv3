package lambda.lambda1;

import lambda.MyFunction;

public class MyFunction1 {

    public static void main(String[] args) {
        MyFunction myfunction = new MyFunction() {
            @Override
            public int apply(int a, int b) {
                return a + b;
            }
        };

        int apply = myfunction.apply(1, 2);
        System.out.println("apply = " + apply);
    }
}
