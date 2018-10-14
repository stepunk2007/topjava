package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            InMemoryMealRepositoryImpl repository = appCtx.getBean(InMemoryMealRepositoryImpl.class);


            MealRestController mealController = appCtx.getBean(MealRestController.class);
            mealController.getAll().forEach(System.out::println);
//
//            Meal meal = mealController.create(new Meal(SecurityUtil.authUserId(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
//            mealController.getAll().forEach(System.out::println);
//
//
//            Meal foreignMeal = repository.get(1, 0);
//            try {
//                mealController.update(foreignMeal, 1);
//                System.out.println("Foreign Meal has been updated id = " + foreignMeal.getId());
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//
//            try {
//                mealController.update(meal, meal.getId());
//                System.out.println("Meal has been updated id = " + meal.getId());
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//
//            try {
//                mealController.delete(123);
//                System.out.println("Meal has been deleted id = 123");
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            mealController.getAll().forEach(System.out::println);
//            try {
//                mealController.delete(meal.getId());
//                System.out.println("Meal has been deleted id = "+meal.getId());
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            repository.getAllByUserId(0).forEach(System.out::println);
//            System.out.println("----------------------");
//            mealController.getAllFilteredByTime(LocalTime.MIN,LocalTime.MAX).forEach(System.out::println);
        }
    }
}
