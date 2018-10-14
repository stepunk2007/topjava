package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController extends AbstractMealController {

    public List<MealWithExceed> getAll() {
        return super.getAll(SecurityUtil.authUserId());
    }

    public List<MealWithExceed> getAllFilteredByDate(LocalDate startDate, LocalDate endDate) {
        return super.getAllFilteredByDate(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay(), startDate, endDate);
    }

    public List<MealWithExceed> getAllFilteredByTime(LocalTime startTime, LocalTime endTime) {
        return super.getAllFilteredByTime(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal create(Meal meal) {
        meal.setUserId(SecurityUtil.authUserId());
        return super.create(meal);
    }

    public void delete(int id) {
        super.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, Integer id) {
        super.update(meal, id, SecurityUtil.authUserId());
    }

}