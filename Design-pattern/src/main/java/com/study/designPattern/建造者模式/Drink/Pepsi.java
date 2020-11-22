package com.study.designPattern.建造者模式.Drink;

public class Pepsi extends ColdDrink {
    @Override
    public float price() {
        return 30.5f;
    }

    @Override
    public String name() {
        return "pepsi";
    }
}
