package com.study.designPattern.简单工厂模式;

public class LenovoComputer implements Computer{
    @Override
    public void start() {
        System.out.println("联想电脑启动");
    }
}
