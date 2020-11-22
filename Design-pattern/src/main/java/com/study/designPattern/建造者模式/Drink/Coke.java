package com.study.designPattern.建造者模式.Drink;

public class Coke extends ColdDrink {
    @Override
    public float price() {
        return 30.0f;
    }

    @Override
    public String name() {
        return "coke";
    }
}
