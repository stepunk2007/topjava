package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by User on 08.10.2018.
 */
public class MemoryMealDao {
    private final AtomicInteger id = new AtomicInteger(1);

    private int getNext() {
        return id.getAndIncrement();
    }

    private Map<Integer, Meal> meals = Arrays.asList(
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 28, 10, 0), "Завтрак", 500),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 28, 13, 0), "Обед", 1000),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 28, 20, 0), "Ужин", 500),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 1000),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 29, 13, 0), "Обед", 500),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 29, 20, 0), "Ужин", 510),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(getNext(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    ).stream()
            .collect(Collectors.toConcurrentMap(Meal::getId, meal -> meal));


    public Meal get(Integer id) {
        return meals.get(id);
    }

    public Collection<Meal> getAll() {
        return meals.values();
    }

    public boolean add(Meal meal) {
        Integer id = getNext();
        meal.setId(id);
        return meals.put(id, meal) == null;
    }

    public Meal remove(int id) {
        return meals.remove(id);
    }

    public boolean update(Meal meal) {
        return meals.put(meal.getId(), meal) != null;
    }
}

