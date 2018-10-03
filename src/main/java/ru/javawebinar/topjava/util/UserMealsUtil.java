package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 10000)
        );
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println(filteredWithExceeded.get(0).exceed);
        System.out.println(filteredWithExceeded.get(1).exceed);
        System.out.println(filteredWithExceeded.get(2).exceed);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .filter(i -> TimeUtil.isBetween(i.getDateTime().toLocalTime(), startTime, endTime))
                .map(i -> new UserMealWithExceed(
                        i.getDateTime(),
                        i.getDescription(),
                        i.getCalories(),
                        mealList.stream()
                                .filter(j -> j.getDateTime().getDayOfYear() == i.getDateTime().getDayOfYear())
                                .map(UserMeal::getCalories)
                                .reduce(0, (acc, x) -> acc + x) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
