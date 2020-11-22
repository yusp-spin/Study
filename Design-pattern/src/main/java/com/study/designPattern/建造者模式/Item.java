package com.study.designPattern.建造者模式;

import com.study.designPattern.建造者模式.pack.Packing;

public interface Item {
    String name();
    Packing packing();
    float price();
}
