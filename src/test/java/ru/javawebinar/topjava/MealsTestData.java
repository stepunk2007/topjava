package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by User on 21.10.2018.
 */
public class MealsTestData {
    public static final Integer USER_BREAKFAST_ID = 100002;
    public static final Integer USER_LUNCH_ID = 100003;
    public static final Integer USER_DINNER_ID = 100004;
    public static final Integer USER_BREAKFAST_ID_2 = 100005;
    public static final Integer USER_BREAKFAST_ID_3 = 100006;

    public static final Meal USER_BREAKFAST = new Meal(USER_BREAKFAST_ID, LocalDateTime.of(2018, Month.OCTOBER, 19, 9, 30), "завтрак", 500);
    public static final Meal USER_LUNCH = new Meal(USER_LUNCH_ID, LocalDateTime.of(2018, Month.OCTOBER, 19, 13, 30), "обед", 1000);
    public static final Meal USER_DINNER = new Meal(USER_DINNER_ID, LocalDateTime.of(2018, Month.OCTOBER, 19, 19, 30), "ужин", 1000);
    public static final Meal USER_BREAKFAST_2 = new Meal(USER_BREAKFAST_ID_2, LocalDateTime.of(2018, Month.OCTOBER, 20, 9, 30), "завтрак", 2001);
    public static final Meal USER_BREAKFAST_3 = new Meal(USER_BREAKFAST_ID_3, LocalDateTime.of(2018, Month.OCTOBER, 21, 9, 30), "завтрак", 300);

    public static final Integer ADMIN_BREAKFAST_ID = 100007;
    public static final Meal ADMIN_BREAKFAST = new Meal(ADMIN_BREAKFAST_ID, LocalDateTime.of(2018, Month.OCTOBER, 19, 8, 30), "завтрак", 600);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(List<Meal> actual, Meal... expected) {
        assertThat(actual).usingFieldByFieldElementComparator().containsExactly(expected);
    }
}
