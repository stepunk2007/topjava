package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;


public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Override
    @Transactional
    Meal save(Meal meal);

    Meal getByIdAndUserId(int id, int userId);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user where m.id = :id and m.user = :user")
    Meal getWithUser(@Param("id") int id, @Param("user") User user);

    List<Meal> findByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> findByUserIdAndDateTimeBetweenOrderByDateTimeDesc(int userId, LocalDateTime startDate, LocalDateTime endDate);

    @Transactional
    Integer deleteByIdAndUserId(int id, int userId);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM Meal m WHERE m.id=?1 AND m.user.id=?2")
//    int delete(int id, int userId);
}
