package ru.javawebinar.topjava.web.user;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.AbstractMealController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ajax/user/meals")
public class MealsAjaxController extends AbstractMealController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @PostMapping
    public void add(@RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                    @RequestParam("description") String description,
                    @RequestParam("calories") int calories) {
        super.create(new Meal(dateTime, description, calories));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }
}
