package com.study.shejimoshi.抽象工厂模式.color;

public class Green implements Color {
    @Override
    public void fill() {
        System.out.println("Inside Green::fill() method.");
    }
}
