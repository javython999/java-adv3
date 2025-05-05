package optional;

import optional.model.Address;
import optional.model.User;

import java.util.Optional;

public class AddressMain2 {

    public static void main(String[] args) {

        User user1 = new User("user1", null);
        User user2 = new User("user2", new Address("hello street"));

        printStreet(user1);
        printStreet(user2);
    }

    private static void printStreet(User user) {
        getUserStreet(user).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Unknown")
        );
    }

    private static Optional<String> getUserStreet(User user) {
        // map 체이닝 중간에 null이면 Optional.empty()를 반환
        return Optional.ofNullable(user)
                .map(User::getAddress)
                .map(Address::getStreet);
    }
}
