package com.study.shejimoshi.工厂方法模式;

public class SteamedBunMachine implements MachineApi{
    @Override
    public void process(String material) {
        System.out.println("我把" + material + "加工成了馒头");
    }
}
