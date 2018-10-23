package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealsTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal userBreakfast = service.get(USER_BREAKFAST_ID, USER_ID);
        assertMatch(userBreakfast, USER_BREAKFAST);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(USER_BREAKFAST_ID, ADMIN_ID);
    }

    @Test
    public void delete() throws Exception {
        int sizeBeforeDelete = service.getAll(USER_ID).size();
        service.delete(USER_BREAKFAST_ID, USER_ID);
        int sizeAfterDelete = service.getAll(USER_ID).size();
        Assert.assertEquals(1, sizeBeforeDelete - sizeAfterDelete);
        assertMatch(service.getAll(USER_ID), USER_BREAKFAST_3, USER_BREAKFAST_2, USER_DINNER, USER_LUNCH);
    }

    @Test
    public void deleteAll() {
        service.delete(ADMIN_BREAKFAST_ID, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(USER_BREAKFAST_ID, ADMIN_ID);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        List<Meal> betweenDateTimes = service.getBetweenDateTimes(LocalDateTime.of(2018, Month.OCTOBER, 19, 9, 30), LocalDateTime.of(2018, Month.OCTOBER, 19, 20, 30), USER_ID);
        Assert.assertEquals(3, betweenDateTimes.size());
        assertMatch(betweenDateTimes, USER_DINNER, USER_LUNCH, USER_BREAKFAST);
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        List<Meal> betweenDateTimes = service.getBetweenDates(LocalDate.of(2018, Month.OCTOBER, 19), LocalDate.of(2018, Month.OCTOBER, 21), USER_ID);
        Assert.assertEquals(5, betweenDateTimes.size());
        assertMatch(betweenDateTimes, USER_BREAKFAST_3, USER_BREAKFAST_2, USER_DINNER, USER_LUNCH, USER_BREAKFAST);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, USER_BREAKFAST_3, USER_BREAKFAST_2, USER_DINNER, USER_LUNCH, USER_BREAKFAST);
    }

    @Test
    public void update() throws Exception {
        Meal updatedMeal = new Meal(USER_BREAKFAST);
        updatedMeal.setDescription("updated breakfast");
        service.update(updatedMeal, USER_ID);
        assertMatch(service.get(updatedMeal.getId(), USER_ID), updatedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updatedMeal = new Meal(USER_BREAKFAST);
        updatedMeal.setDescription("updated breakfast");
        service.update(updatedMeal, ADMIN_ID);
    }

    @Test
    public void testCreate() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2018, Month.OCTOBER, 22, 9, 30), "Завтрак", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), newMeal, USER_BREAKFAST_3, USER_BREAKFAST_2, USER_DINNER, USER_LUNCH, USER_BREAKFAST);
    }

    @Test(expected = DuplicateKeyException.class)
    public void testCreateDuplicateKeyException() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2018, Month.OCTOBER, 20, 9, 30), "Завтрак", 500);
        service.create(newMeal, USER_ID);
    }
}