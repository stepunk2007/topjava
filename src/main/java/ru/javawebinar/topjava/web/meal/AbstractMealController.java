package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

/**
 * Created by User on 14.10.2018.
 */
public class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    protected Meal get(Integer id, Integer userId) {
        return service.get(id, userId);
    }

    protected List<MealWithExceed> getAll(Integer userId, int caloriesPerDay) {
        return service.getAll(userId, caloriesPerDay);
    }

    protected List<MealWithExceed> getAllFilteredByDateTime(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return service.getAllFilteredByDateTime(userId, caloriesPerDay, startDate, endDate, startTime, endTime);
    }

    protected Meal create(Meal meal) {
        checkNew(meal);
        return service.create(meal);
    }

    protected void delete(int id, Integer userId) {
        service.delete(id, userId);
    }

    protected void update(Meal meal, Integer mealId) {
        assureIdConsistent(meal, mealId);
        service.update(meal);
    }
}
