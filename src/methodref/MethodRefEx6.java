package methodref;

import java.util.function.BiFunction;

public class MethodRefEx6 {

    public static void main(String[] args) {
        Person person = new Person("Kim");

        // 람다
        BiFunction<Person, Integer, String> function1 = (Person p, Integer n) -> p.introduceWithName(n);
        System.out.println("person.introduceWithNumber = " + function1.apply(person, 1));

        // 메서드 참조, 타입이 첫 번째 매개변수, 첫 번째 매개변수의 메서드를 호출
        BiFunction<Person, Integer, String> function2 = Person::introduceWithName;
        System.out.println("person.introduceWithNumber = " + function2.apply(person, 1));
    }
}
