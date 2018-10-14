package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

/**
 * Created by User on 14.10.2018.
 */
public class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll(Integer userId) {
        return service.getAll(userId, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getAllFilteredByDate(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate) {
        return service.getAllFilteredByDate(userId, caloriesPerDay, startDate, endDate);
    }

    public List<MealWithExceed> getAllFilteredByTime(int userId, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        checkNotNull(startTime, endTime);
        return service.getAllFilteredByTime(userId, caloriesPerDay, startTime, endTime);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id, Integer userId) {
        service.delete(id, userId);
    }

    public void update(Meal meal, Integer mealId, Integer userId) {
        assureIdConsistent(meal, mealId);
        service.update(meal, userId);
    }
}
