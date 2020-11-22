package com.study.shejimoshi.抽象工厂模式;

import com.study.shejimoshi.抽象工厂模式.Factory.AbstractFactory;
import com.study.shejimoshi.抽象工厂模式.Factory.ColorFactory;
import com.study.shejimoshi.抽象工厂模式.Factory.ShapeFactory;

/**
 * 抽象工厂模式是所有形式的工厂模式中最为抽象和最具一般性的一种形态。抽象工厂
 * 模式与工厂方法模式最大的区别在于：工厂方法模式针对的是一个产品等级结构，而抽象
 * 工厂模式则需要面对多个产品等级结构。
 */
public class FactoryProducer {
    public static AbstractFactory getFactory (String choice) {
        if(choice.equalsIgnoreCase("SHAPE")) {
            return new ShapeFactory();
        }else if(choice.equalsIgnoreCase("COLOR")) {
            return new ColorFactory();
        }
        return null;
    }
}
