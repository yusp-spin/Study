package com.study.designPattern.工厂方法模式;

public class test {
    public static void main(String[] args) {
        Factory factory1  = new SteamedBunFactory ();
        factory1.process("面粉");//我把面粉加工成了馒头

        Factory factory2  = new NoodleFactory ();
        factory2.process("米粉");//我把面粉加工成了馒头
    }
}
