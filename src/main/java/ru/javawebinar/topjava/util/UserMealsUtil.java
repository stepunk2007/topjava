package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<Integer, Integer> dayCaloriesMap = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            dayCaloriesMap.merge(userMeal.getDateTime().getDayOfYear(), userMeal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
        }

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                userMealWithExceedList.add(
                        new UserMealWithExceed(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                dayCaloriesMap.get(userMeal.getDateTime().getDayOfYear()) > caloriesPerDay)
                );
        }
        return userMealWithExceedList;
    }
}
