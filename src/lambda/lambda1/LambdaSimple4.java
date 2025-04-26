package lambda.lambda1;

public class LambdaSimple4 {

    public static void main(String[] args) {
        // 기본
        MyCall call1 = (int value) -> value * 2;

        // 타입 추론
        MyCall call2 = (value) -> value * 2;

        // 매개변수 괄호 생략 (매개 변수가 1개일 때)
        MyCall call3 = value -> value * 2;
    }

    interface MyCall {
        int call(int value);
    }
}
