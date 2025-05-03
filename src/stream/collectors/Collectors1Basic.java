package stream.collectors;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Collectors1Basic {

    public static void main(String[] args) {
        // 기본 기능
        List<String> list = Stream.of("Java", "Spring", "JPA")
                .collect(Collectors.toList());
        System.out.println("list = " + list);

        // 수정 불가능 리스트
        List<Integer> unmodifiableNumberList = Stream.of(1, 2, 3)
                .collect(Collectors.toUnmodifiableList());
        //unmodifiableNumberList.add(4);
        System.out.println("unmodifiableNumberList = " + unmodifiableNumberList);

        Set<Integer> set = Stream.of(1, 2, 2, 3, 3)
                .collect(Collectors.toSet());
        System.out.println("set = " + set);

        // 타입 지정
        TreeSet<Integer> treeSet = Stream.of(3, 4, 5, 2, 1)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("treeSet = " + treeSet);
    }
}
