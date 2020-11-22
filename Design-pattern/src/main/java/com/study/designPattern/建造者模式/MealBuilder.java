package com.study.designPattern.建造者模式;

import com.study.designPattern.建造者模式.Burger.VegBurder;
import com.study.designPattern.建造者模式.Burger.chickenBurger;
import com.study.designPattern.建造者模式.Drink.Coke;
import com.study.designPattern.建造者模式.Drink.Pepsi;

public class MealBuilder {
    public Meal prepareVegMeal () {
        Meal meal = new Meal();
        meal.addItem(new VegBurder());
        meal.addItem(new Coke());
        return meal;
    }

    public Meal prepareNonVegMeal () {
        Meal meal = new Meal();
        meal.addItem(new chickenBurger());
        meal.addItem(new Pepsi());
        return meal;
    }
}
