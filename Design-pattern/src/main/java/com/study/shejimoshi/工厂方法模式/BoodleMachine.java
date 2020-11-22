package com.study.shejimoshi.工厂方法模式;

public class BoodleMachine implements MachineApi{
    @Override
    public void process(String material) {
        System.out.println("我把" + material + "加工成了面条");
    }
}
