package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Integer id, Meal meal);

    // false if meal do not belong to userId
    boolean delete(Integer idUser, Integer idMeal);

    // null if meal do not belong to userId
    Meal get(Integer idUser, Integer idMeal);

    // ORDERED dateTime desc
    Collection<Meal> getAll(Integer idUser);
}
