package com.study.designPattern.抽象工厂模式.Factory;

import com.study.designPattern.抽象工厂模式.color.Color;
import com.study.designPattern.抽象工厂模式.shape.Circle;
import com.study.designPattern.抽象工厂模式.shape.Rectangle;
import com.study.designPattern.抽象工厂模式.shape.Shape;
import com.study.designPattern.抽象工厂模式.shape.Square;

public class ShapeFactory extends AbstractFactory {
    @Override
    public Color getColor(String color) {
        return null;
    }

    @Override
    public Shape getShape(String shapeType) {
        if(shapeType == null) {
            return null;
        }
        if(shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle();
        }else if (shapeType.equalsIgnoreCase("RECTANGLE")) {
            return new Rectangle();
        }else if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square();
        }
        return null;
    }
}
