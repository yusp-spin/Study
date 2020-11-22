package com.study.shejimoshi.抽象工厂模式.Factory;

import com.study.shejimoshi.抽象工厂模式.color.Blue;
import com.study.shejimoshi.抽象工厂模式.color.Color;
import com.study.shejimoshi.抽象工厂模式.color.Green;
import com.study.shejimoshi.抽象工厂模式.color.Red;
import com.study.shejimoshi.抽象工厂模式.shape.Shape;

public class ColorFactory extends AbstractFactory {
    @Override
    public Color getColor(String color) {
        if(color == null) {
            return null;
        }
        if(color.equalsIgnoreCase("RED")) {
            return new Red();
        }else if(color.equalsIgnoreCase("GREEN")) {
            return new Green();
        }else if (color.equalsIgnoreCase("BLUE")) {
            return new Blue();
        }
        return null;
    }

    @Override
    public Shape getShape(String shape) {
        return null;
    }
}
