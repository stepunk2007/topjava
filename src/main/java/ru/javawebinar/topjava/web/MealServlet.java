package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealService mealService = new MealService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action") == null ? "get" : request.getParameter("action");
            switch (action.toLowerCase()) {
                case "edit":
                    request.setAttribute("meal", mealService.getMealWithExceeded().get(0));
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    log.debug("forward to edit page");
                    break;
                case "delete":
                    mealService.remove(Integer.parseInt(request.getParameter("id")));
                default:
                    request.setAttribute("meals", mealService.getMealWithExceeded());
                    log.debug("forward to meals list");
                    request.getRequestDispatcher("/meals.jsp").forward(request, response);
            }
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } catch (Exception e) {
            //todo redirect to Error Page
            log.error(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Meal meal = createMeal(req);
            if (meal.getId() != null) {
                mealService.update(createMeal(req));
                log.debug("Meal updated");
            } else {
                mealService.add(createMeal(req));
                log.debug("New Meal added");
            }

            resp.sendRedirect("meals");
        } catch (Exception e) {
            //todo redirect to Error Page
            log.error(e.getMessage());
        }
    }

    private Meal createMeal(HttpServletRequest req) {
        return new Meal(
                req.getParameter("id") == null ? null : Integer.parseInt(req.getParameter("id")),
                LocalDateTime.parse(req.getParameter("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));
    }
}
