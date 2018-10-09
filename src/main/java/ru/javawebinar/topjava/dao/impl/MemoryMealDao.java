package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by User on 08.10.2018.
 */
public class MemoryMealDao implements Dao {
    private static class Counter {
        private static int count;

        public static synchronized int getNext() {
            return count++;
        }
    }

    private List<Meal> meals = new ArrayList<>();

    {
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 28, 10, 0), "Завтрак", 500));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 28, 13, 0), "Обед", 1000));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 28, 20, 0), "Ужин", 500));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 29, 13, 0), "Обед", 500));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 29, 20, 0), "Ужин", 510));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(Counter.getNext(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public List<Meal> get() {
        return meals;
    }

    @Override
    public boolean add(Meal meal) {
        meal.setId(Counter.getNext());
        return meals.add(meal);
    }

    @Override
    public boolean remove(int id) {
        return meals.removeIf(a -> a.getId() == id);
    }

    @Override
    public void update(Meal newMeal) {
        meals = meals.stream()
                .map(old -> old.getId() == newMeal.getId() ? newMeal : old)
                .collect(toList());

    }
}

