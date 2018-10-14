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
    public Meal get(Integer id) {
        return super.get(id, SecurityUtil.authUserId());
    }

    public List<MealWithExceed> getAll() {
        return super.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return super.getAllFilteredByDateTime(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay(), startDate, endDate, startTime, endTime);
    }

    public Meal create(Meal meal) {
        meal.setUserId(SecurityUtil.authUserId());
        return super.create(meal);
    }

    public void delete(int id) {
        super.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, Integer id) {
        meal.setUserId(SecurityUtil.authUserId());
        super.update(meal, id);
    }

}