package com.study.designPattern.简单工厂模式;

public class HpComputer implements Computer {
    @Override
    public void start() {
        System.out.println("惠普电脑启动");
    }
}
