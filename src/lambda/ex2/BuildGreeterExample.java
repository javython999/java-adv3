package lambda.ex2;

public class BuildGreeterExample {


    public static void main(String[] args) {
        StringFunction helloGreeter = buildGreeter("Hello");
        StringFunction hiGreeter = buildGreeter("Hi");

        System.out.println(helloGreeter.apply("Java"));
        System.out.println(hiGreeter.apply("Java"));

    }

    private static StringFunction buildGreeter(String greeting) {
        return str -> greeting + ", " + str;
    }
}
