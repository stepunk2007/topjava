package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {
    Meal create(Meal meal);

    void delete(int id, Integer userId) throws NotFoundException;

    Meal get(int id, Integer userId) throws NotFoundException;

    void update(Meal meal, Integer userId) throws NotFoundException;

    List<MealWithExceed> getAll(Integer userId, int caloriesPerDay);

    List<MealWithExceed> getAllFilteredByDate(Integer userId, int caloriesPerDay, LocalDate startTime, LocalDate endTime);

    List<MealWithExceed> getAllFilteredByTime(Integer userId, int caloriesPerDay, LocalTime startTime, LocalTime endTime);


}