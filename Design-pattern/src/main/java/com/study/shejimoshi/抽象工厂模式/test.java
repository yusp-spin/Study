package com.study.shejimoshi.抽象工厂模式;

import com.study.shejimoshi.抽象工厂模式.Factory.AbstractFactory;
import com.study.shejimoshi.抽象工厂模式.color.Color;
import com.study.shejimoshi.抽象工厂模式.shape.Shape;

public class test {
    public static void main(String[] args) {
        AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");
        Shape shape1 = shapeFactory.getShape("CIRCLE");
        shape1.draw();

        AbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");
        Color red = colorFactory.getColor("RED");
        red.fill();
    }
}
