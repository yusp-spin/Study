package com.study.shejimoshi.单例模式;

/**
 * 单例（Singleton）模式的定义：指一个类只有一个实例，且该类能自行创建这个实例的一种模式
 * 单例模式有 3 个特点：
 * 单例类只有一个实例对象；
 * 该单例对象必须由单例类自行创建；
 * 单例类对外提供一个访问该单例的全局访问点。
 */
public class Singleton {

}


//懒汉式
class LazySingleton {
    private static volatile LazySingleton instance ;
    private LazySingleton() {}

    public static synchronized LazySingleton getInstance() {
        if(instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}

//饿汉式
class HungrySingleton {
    private static HungrySingleton instance = new HungrySingleton();
    private HungrySingleton () {}
    public static HungrySingleton getInstance() {
        return instance;
    }
}


//双重校验锁

class DoubleSingleton {
    private static volatile DoubleSingleton instance;
    private DoubleSingleton (){}

    public static DoubleSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleSingleton.class) {
                if (instance == null) {
                    instance = new DoubleSingleton();
                }
            }
        }
        return instance;
    }
}