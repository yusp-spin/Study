package com.study.shejimoshi.工厂方法模式;

public class NoodleFactory extends Factory {
    @Override
    public MachineApi newMachine() {
        return new BoodleMachine();
    }
}
