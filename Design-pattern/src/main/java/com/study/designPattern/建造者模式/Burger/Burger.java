package com.study.designPattern.建造者模式.Burger;

import com.study.designPattern.建造者模式.Item;
import com.study.designPattern.建造者模式.pack.Packing;
import com.study.designPattern.建造者模式.pack.Wrapper;

public abstract class Burger implements Item {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

    @Override
    public abstract float price();
}
