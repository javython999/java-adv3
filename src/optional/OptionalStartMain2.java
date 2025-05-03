package optional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionalStartMain2 {

    private static final Map<Long, String> map = new HashMap<>();

    static {
        map.put(1L, "Kim");
        map.put(2L, "Seo");
    }

    public static void main(String[] args) {
        findAndPrint(1L);
        findAndPrint(3L);
    }

    private static void findAndPrint(Long id) {
        String name = findNameById(id).orElse("UNKNOWN");
        System.out.println(id +": " + name.toUpperCase());
    }

    private static Optional<String> findNameById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

}
