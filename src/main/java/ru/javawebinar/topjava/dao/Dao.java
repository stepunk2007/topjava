package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by User on 10.10.2018.
 */
public interface Dao {
    List<Meal> get();

    boolean add(Meal meal);

    boolean remove(int id);

    void update(Meal meal);
}
