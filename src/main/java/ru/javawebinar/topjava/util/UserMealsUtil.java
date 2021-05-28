package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        Map<LocalDate,Integer> mapExcess = new HashMap<>();

        for (UserMeal meal : meals){
            LocalDate localDate = meal.getDateTime().toLocalDate();
            if(!mapExcess.containsKey(localDate)){
               mapExcess.put(localDate, meal.getCalories());
            } else {
                int countCalories = mapExcess.get(localDate);
                mapExcess.put(localDate, countCalories + meal.getCalories());
            }
        }

        for (UserMeal meal : meals){
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)){
                userMealWithExcessList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), (mapExcess.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)));
            }
        }

        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> userMealWithExcessList;

        List<UserMeal> userMealsWithFilter = meals.stream().filter((a) -> TimeUtil.isBetweenHalfOpen(a.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
        Map<LocalDate, List<UserMeal>> mapUserMeals = meals.stream().collect(Collectors.groupingBy((a -> a.getDateTime().toLocalDate())));

        userMealWithExcessList = userMealsWithFilter.stream()
                .map(e -> new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), (mapUserMeals.get(e.getDateTime().toLocalDate()).stream().map(a -> e.getCalories()).reduce(Integer::sum).get() > caloriesPerDay)))
                .collect(Collectors.toList());


        return userMealWithExcessList;
    }
}
