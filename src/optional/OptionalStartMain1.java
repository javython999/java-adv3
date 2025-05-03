package optional;

import java.util.HashMap;
import java.util.Map;

public class OptionalStartMain1 {

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
        String name = findNameById(id);
        // 1. NullPointerException
        //System.out.println("name = " + name.toUpperCase());
        if (name != null) {
            System.out.println(id +": " + name.toUpperCase());
        } else {
            System.out.println(id + ": UNKNOWN");
        }
    }

    private static String findNameById(Long id) {
        return map.get(id);
    }

}
