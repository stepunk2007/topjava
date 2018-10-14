package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 13.10.2018.
 */
public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User(null, "admin", "admin@mail.ru", "admin", Role.ROLE_ADMIN),
            new User(null, "stepa", "stepa@mail.ru", "stepa", Role.ROLE_USER),
            new User(null, "tanya", "tanya@mail.ru", "tanya", Role.ROLE_USER)
    );
}
