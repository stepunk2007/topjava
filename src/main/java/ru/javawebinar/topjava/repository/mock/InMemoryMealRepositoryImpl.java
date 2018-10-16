package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> this.save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return new Meal(meal);
        }
        Meal oldMeal = repository.get(meal.getId());
        if (oldMeal != null && !oldMeal.getUserId().equals(meal.getUserId())) {
            return null;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldValue) -> new Meal(meal));
    }

    @Override
    public boolean delete(int id, Integer userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId().equals(userId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId().equals(userId) ? new Meal(meal) : null;
    }

    @Override
    public Collection<Meal> getAllByUserId(Integer userId) {
        return getAllByUserId(userId, meal -> true);
    }

    @Override
    public Collection<Meal> getAllByUserId(Integer userId, LocalDate startDate, LocalDate endDate) {
        return getAllByUserId(userId, meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalDate(), startDate, endDate));
    }

    private Collection<Meal> getAllByUserId(Integer userId, Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(m -> m.getUserId().equals(userId))
                .filter(filter)
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .map(Meal::new)
                .collect(Collectors.toList());
    }
}

