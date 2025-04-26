package lambda.start;

public class Ex0RefMain {

    public static void hello(String string) {
        System.out.println("프로그래밍 시작");
        System.out.println(string);
        System.out.println("프로그램 종료");
    }

    public static void main(String[] args) {
        hello("Hello Java");
        hello("Hello Spring");
    }
}
