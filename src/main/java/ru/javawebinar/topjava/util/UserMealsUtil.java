package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 10000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceededByCycle(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).stream()
                .forEach(System.out::println);
        getFilteredWithExceededByStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).stream()
                .forEach(System.out::println);

    }

    public static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCaloriesMap = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            dayCaloriesMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                userMealWithExceedList.add(
                        new UserMealWithExceed(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                dayCaloriesMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)
                );
        }
        return userMealWithExceedList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCaloriesMap = mealList.stream()
                .collect(Collectors.toMap(userMeal -> userMeal.getDateTime().toLocalDate(),
                        UserMeal::getCalories,
                        Integer::sum));
        return mealList.stream()
                .filter(a -> TimeUtil.isBetween(a.getDateTime().toLocalTime(), startTime, endTime))
                .map(a -> new UserMealWithExceed(a.getDateTime(),
                        a.getDescription(),
                        a.getCalories(),
                        dayCaloriesMap.get(a.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

    }
}
