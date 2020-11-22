package com.study.designPattern.工厂方法模式;

/**
 * 工厂方法模式（Factory Method Pattern）：又称为工厂模式或者多态工厂模式。在
 * 工厂方法模式中，工厂父类负责定义创建产品对象的公共接口，而工厂子类则负责生成具
 * 体的产品对象，这样做的目的是将产品的实例化操作延迟到工厂子类中完成，即通过工厂
 * 子类来确定究竟应该实例化哪一个具体产品类。
 */
public abstract class Factory {
    public abstract MachineApi newMachine();

    public void process (String material) {
        MachineApi machine = newMachine();
        machine.process(material);
    }

}
