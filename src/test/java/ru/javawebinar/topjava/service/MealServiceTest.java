package ru.javawebinar.topjava.service;

import org.junit.AssumptionViolatedException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionSystemException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = getLogger(MealServiceTest.class);

    static {
        SLF4JBridgeHandler.install();
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @ClassRule
    public static final TimeWatcher testsTimeWatcher = new TimeWatcher();

    private static class TimeWatcher extends TestWatcher {
        private List<String> logInfoList = new LinkedList<>();

        public void logInfo(Description description, String status, Long duration) {
            String logInfo = String.format("Test %s %s, spent %d microseconds",
                    description.getMethodName(), status, TimeUnit.NANOSECONDS.toMicros(duration));
            log.info(logInfo);
            logInfoList.add(logInfo);
        }

        @Override
        protected void finished(Description description) {
            logInfoList.forEach(log::info);
        }
    }

    @Rule
    public final Stopwatch methodTimeWatcher = new Stopwatch() {
        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            testsTimeWatcher.logInfo(description, "failed", nanos);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            testsTimeWatcher.logInfo(description, "skipped", nanos);
        }

        @Override
        protected void succeeded(long nanos, Description description) {
            testsTimeWatcher.logInfo(description, "succeeded", nanos);
        }
    };


    @Autowired
    private MealService service;

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() throws Exception {
        exception.expect(NotFoundException.class);
        exception.expectMessage("Not found entity with id=" + MEAL1_ID);
        service.delete(MEAL1_ID, 1);
    }

    @Test
    public void create() throws Exception {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(service.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        exception.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        exception.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void descriptionNotValid() throws Exception {
        exception.expect(TransactionSystemException.class);
        Meal updated = getUpdated();
        updated.setDescription(null);
        service.update(updated, USER_ID);
    }

    @Test
    public void caloriesNotValid() throws Exception {
        exception.expect(TransactionSystemException.class);
        Meal updated = getUpdated();
        updated.setCalories(5001);
        service.update(updated, USER_ID);
    }
}