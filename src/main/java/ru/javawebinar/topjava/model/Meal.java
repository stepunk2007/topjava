package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.ALL_SORTED, query = "select m from Meal m where m.user.id =:userId order by m.dateTime desc"),
        @NamedQuery(name = Meal.DELETE, query = "delete from Meal m where m.user.id =:userId and m.id =:id"),
        @NamedQuery(name = Meal.BETWEEN, query = "select m from Meal m where  m.user.id =:userId and m.dateTime between :startDate and :endDate order by m.dateTime desc")
})
@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","date_time"}))
public class Meal extends AbstractBaseEntity {
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String DELETE = "Meal.delete";
    public static final String BETWEEN = "Meal.getBetween";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotNull
    @Size(min = 2,max = 120, message = "Размер поля \"Описание\" должен быть между 2 и 120")
    private String description;

    @Column(name = "calories", nullable = false)
    @Range(min = 10,max = 5000, message = "Поле \"Калории\" должно быть между 10 и 5000")
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
