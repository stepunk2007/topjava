package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.assertMatch;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {
    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getWithMealsNotFound(){
        thrown.expect(NotFoundException.class);
        service.getWithMeals(1);
    }

    @Test
    public void getUserWithoutMeals(){
        User user = service.getWithMeals(USER_WITHOUT_MEAL_ID);
        assertMatch(user, USER_WITHOUT_MEAL);
        MealTestData.assertMatch(user.getMeals(), List.of());
    }
}
