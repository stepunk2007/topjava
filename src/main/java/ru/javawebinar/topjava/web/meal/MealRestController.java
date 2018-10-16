package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public Meal get(Integer id) {
        return service.get(id, SecurityUtil.authUserId());
    }

    public List<MealWithExceed> getAll() {
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay(),
                startDate == null ? LocalDate.MIN : startDate,
                endDate == null ? LocalDate.MAX : endDate,
                startTime == null ? LocalTime.MIN : startTime,
                endTime == null ? LocalTime.MAX : endTime);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, Integer id) {
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

}