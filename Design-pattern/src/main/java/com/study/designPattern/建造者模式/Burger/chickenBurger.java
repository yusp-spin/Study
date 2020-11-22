package com.study.designPattern.建造者模式.Burger;

import com.study.designPattern.建造者模式.Burger.Burger;

public class chickenBurger extends Burger {
    @Override
    public float price() {
        return 50.0f;
    }

    @Override
    public String name() {
        return "chick Burger";
    }
}
