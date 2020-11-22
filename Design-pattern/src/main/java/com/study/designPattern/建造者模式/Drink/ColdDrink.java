package com.study.designPattern.建造者模式.Drink;

import com.study.designPattern.建造者模式.Item;
import com.study.designPattern.建造者模式.pack.Bottle;
import com.study.designPattern.建造者模式.pack.Packing;

public abstract class ColdDrink implements Item {
    @Override
    public Packing packing() {
        return new Bottle();
    }

    public abstract float price();
}
