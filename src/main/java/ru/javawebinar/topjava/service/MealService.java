package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

/**
 * Created by User on 08.10.2018.
 */
public class MealService {
    private MemoryMealRepository repository = new MemoryMealRepository();
    private final static int CALORIES_PER_DAY = 2000;

    public List<MealWithExceed> getMealWithExceeded() {
        return MealsUtil.getWithExceeded(repository.getMeals(), CALORIES_PER_DAY);
    }
}
