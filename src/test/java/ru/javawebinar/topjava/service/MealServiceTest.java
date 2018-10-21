package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static ru.javawebinar.topjava.MealsTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest extends TestCase {

    @Autowired
    private MealService service;

    public void setUp() throws Exception {
        super.setUp();

    }

    @Test(expected = NotFoundException.class)
    public void get() throws Exception {
        Meal userBreakfast = service.get(USER_BREAKFAST_ID, USER_ID);
        assertMatch(userBreakfast, USER_BREAKFAST);

        service.get(USER_BREAKFAST_ID, ADMIN_ID);
    }

    @Test
    public void delete() throws Exception {
        int sizeBeforeDelete = service.getAll(USER_ID).size();
        service.delete(USER_BREAKFAST_ID, USER_ID);
        int sizeAfterDelete = service.getAll(USER_ID).size();
        assertNotEquals(sizeBeforeDelete, sizeAfterDelete);
        Assert.assertEquals(1, sizeBeforeDelete - sizeAfterDelete);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(USER_BREAKFAST_ID, ADMIN_ID);
    }


    @Test
    public void testGetBetweenDateTimes() throws Exception {
        List<Meal> betweenDateTimes = service.getBetweenDateTimes(LocalDateTime.of(2018, Month.OCTOBER, 19, 9, 30), LocalDateTime.of(2018, Month.OCTOBER, 19, 10, 30), USER_ID);
        Assert.assertEquals(1, betweenDateTimes.size());

        betweenDateTimes = service.getBetweenDateTimes(LocalDateTime.of(2018, Month.OCTOBER, 19, 9, 30), LocalDateTime.of(2018, Month.OCTOBER, 19, 13, 30), USER_ID);
        Assert.assertEquals(2, betweenDateTimes.size());

        betweenDateTimes = service.getBetweenDateTimes(LocalDateTime.of(2018, Month.OCTOBER, 19, 9, 30), LocalDateTime.of(2018, Month.OCTOBER, 19, 20, 30), USER_ID);
        Assert.assertEquals(3, betweenDateTimes.size());
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, USER_BREAKFAST, USER_LUNCH, USER_DINNER);
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
        Meal newMeal = new Meal(null, LocalDateTime.of(2018, Month.OCTOBER, 20, 9, 30), "Завтрак", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), USER_BREAKFAST, USER_LUNCH, USER_DINNER, newMeal);
    }
}