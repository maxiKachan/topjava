package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private static final Map<Integer, Map<Integer,Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(5, meal));
    }

    @Override
    public Meal save(Integer id, Meal meal) {
        if (!repository.containsKey(id)){
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> tempRepository = repository.get(id);
            tempRepository.put(meal.getId(), meal);
            repository.put(id, tempRepository);
            return meal;
        }
        // handle case: update, but not present in storage
        repository.get(id).put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(Integer idUser, Integer idMeal) {
        Map<Integer, Meal> userMeal = repository.get(idMeal);
        return userMeal.remove(idMeal) != null;
    }

    @Override
    public Meal get(Integer idUser, Integer idMeal) {
        Map<Integer, Meal> userMeal = repository.get(idUser);
        return userMeal.get(idMeal);
    }

    @Override
    public Collection<Meal> getAll(Integer idUser) {
        Map<Integer, Meal> userMeal = repository.get(idUser);
        Comparator <Meal> mealComparator = Comparator.comparing(Meal::getDateTime);
       // return userMeal.values();
        return userMeal.values().stream().sorted(mealComparator.reversed()).collect(Collectors.toList());
    }

    public static Map<Integer, Map<Integer, Meal>> getRepository() {
        return repository;
    }
}

