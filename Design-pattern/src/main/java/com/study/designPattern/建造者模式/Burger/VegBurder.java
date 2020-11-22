package com.study.designPattern.建造者模式.Burger;

public class VegBurder extends Burger {
    @Override
    public float price() {
        return 25.0f;
    }

    @Override
    public String name() {
        return "veg burger";
    }
}
