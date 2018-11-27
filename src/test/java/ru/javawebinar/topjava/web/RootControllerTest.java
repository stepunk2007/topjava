package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    void testMeals() throws Exception {
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals",
                        hasItem(hasProperty("id", is(MEAL1_ID)))))
                .andExpect(model().attribute("meals",
                        hasItem(hasProperty("id", is(MEAL1_ID + 1)))))
                .andExpect(model().attribute("meals",
                        hasItem(hasProperty("id", is(MEAL1_ID + 2)))))
                .andExpect(model().attribute("meals",
                        hasItem(hasProperty("id", is(MEAL1_ID + 3)))))
                .andExpect(model().attribute("meals",
                        hasItem(hasProperty("id", is(MEAL1_ID + 4)))))
                .andExpect(model().attribute("meals",
                        hasItem(hasProperty("id", is(MEAL1_ID + 5)))));


    }

    @Test
    void tetsStyle() throws Exception {
        mockMvc.perform(get("/resources/css/style.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/css"));
    }
}