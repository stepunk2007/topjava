package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.dao.impl.MemoryMealDao;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

/**
 * Created by User on 08.10.2018.
 */
public class MealService {
    private MemoryMealDao dao = new MemoryMealDao();
    private final static int CALORIES_PER_DAY = 2000;

    public List<MealWithExceed> getMealWithExceeded() {
        return MealsUtil.getWithExceeded(dao.get(), CALORIES_PER_DAY);
    }

    public void add(Meal meal){
        dao.add(meal);
    }

    public void remove(int id) {
        dao.remove(id);
    }

    public void update(Meal meal) {
        dao.update(meal);
    }
}
