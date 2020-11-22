package com.study.designPattern.抽象工厂模式.color;

public class Red implements Color {
    @Override
    public void fill() {
        System.out.println("Inside Red::fill() method.");
    }
}
