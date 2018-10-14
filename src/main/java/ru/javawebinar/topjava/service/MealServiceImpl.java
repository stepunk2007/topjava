package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id, Integer userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, Integer userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void update(Meal meal, Integer userId) throws NotFoundException {
        if (userId != null && userId.equals(meal.getUserId())) {
            repository.save(meal);
        } else {
            checkNotFoundWithId(null, meal.getId());
        }

    }

    @Override
    public List<MealWithExceed> getAll(Integer userId, int caloriesPerDay) {
        return MealsUtil.getFilteredWithExceeded(repository.getAllByUserId(userId), caloriesPerDay, LocalTime.MIN, LocalTime.MAX);
    }

    @Override
    public List<MealWithExceed> getAllFilteredByDate(Integer userId, int caloriesPerDay, LocalDate startTime, LocalDate endTime) {
        return MealsUtil.getFilteredWithExceeded(repository.getAllByUserId(userId), caloriesPerDay, startTime, endTime);
    }

    @Override
    public List<MealWithExceed> getAllFilteredByTime(Integer userId, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredWithExceeded(repository.getAllByUserId(userId), caloriesPerDay, startTime, endTime);
    }
}