package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealService mealService;

    @Override
    public void init() throws ServletException {
        super.init();
        mealService = new MealService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action") == null ? "getAll" : request.getParameter("action");
            switch (action.toLowerCase()) {
                case "edit":
                    request.setAttribute("meal", mealService.get(Integer.valueOf(request.getParameter("id"))));
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    log.debug("forward to edit page");
                    break;
                case "delete":
                    mealService.remove(Integer.parseInt(request.getParameter("id")));
                    log.debug("Remove meal with id = %s", request.getParameter("id"));
                    response.sendRedirect("meals");
                    break;
                default:
                    List<MealWithExceed> allWithExceeded = mealService.getAllWithExceeded();
                    request.setAttribute("meals", mealService.getAllWithExceeded());
                    log.debug("forward to meals list");
                    request.getRequestDispatcher("/meals.jsp").forward(request, response);
            }
        } catch (Exception e) {
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
            log.error(e.getMessage());
        }
    }

    private Meal createMeal(HttpServletRequest req) {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return new Meal(
                req.getParameter("id") == null ? null : Integer.parseInt(req.getParameter("id")),
                LocalDateTime.parse(req.getParameter("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));
    }
}
