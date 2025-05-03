package methodref;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MethodRefEx4 {

    public static void main(String[] args) {
        List<Person> personList = List.of(
                new Person("Kim"),
                new Person("Park"),
                new Person("Lee")
        );

        List<String> result1 = mapPersonToString(personList, (Person p) -> p.introduce());
        System.out.println("result1 = " + result1);

        List<String> result2 = mapPersonToString(personList, Person::introduce);
        System.out.println("result2 = " + result2);

        List<String> upperResult1 = mapStringToString(result1, (String s) -> s.toUpperCase());
        System.out.println("upperResult1 = " + upperResult1);

        List<String> upperResult2 = mapStringToString(result1, String::toUpperCase);
        System.out.println("upperResult2 = " + upperResult2);


    }

    private static List<String> mapPersonToString(List<Person> psersonList, Function<Person, String> function) {
        List<String> result = new ArrayList<>();
        for (Person person : psersonList) {
            result.add(function.apply(person));
        }
        return result;
    }

    private static List<String> mapStringToString(List<String> strings, Function<String, String> function) {
        ArrayList<String> result = new ArrayList<>();
        for (String string : strings) {
            result.add(function.apply(string));
        }
        return result;
    }
}
