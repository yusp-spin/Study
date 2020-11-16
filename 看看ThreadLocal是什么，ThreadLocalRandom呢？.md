---
title: 看看ThreadLocal是什么，ThreadLocalRandom呢？
date: 2020-09-15 15:22:47
categories: 并发编程（java）
tags: 线程封闭
---

这两个知识点比较类似，恰巧在面试中也被频繁问起，所以对他两做一个简单的介绍

#### 一、ThreadLocal：

​	指的是线程私有的变量，通过set，get，remove等方法进行操作，对应的底层操作其实是ThreadLocalMap
​	

##### 1.ThreadLocalMap存的是什么

ThreadLocal里的键值对，key是threadlocal的弱引用，value是我们存放的值



##### 2.ThreadLocalMap的key为什么是弱引用

key 使用强引用：引用的ThreadLocal的对象被回收了，但是ThreadLocalMap还持有ThreadLocal的强引用，如果没有手动删除，ThreadLocal不会被回收，导致Entry内存泄漏。 
ThreadLocalMap的key是弱引用，而Value是强引用。这就导致了一个问题，ThreadLocal在没有外部对象强引用时，发生GC时弱引用Key会被回收，而Value不会回收，如果创建ThreadLocal的线程一直持续运行，那么这个Entry对象中的value就有可能一直得不到回收，发生内存泄露。 
我们可以发现：由于ThreadLocalMap的生命周期跟Thread一样长，如果都没有手动删除对应key，都会导致内存泄漏，但是使用弱引用可以多一层保障，set，get remove后面都会调用expungeStateEntry方法，对应的value在下一次ThreadLocalMap调用set,get,remove的时候会被清除。



##### 3.ThreadLocalMap是怎么解决哈希冲突的

开放寻址法



##### 4 ThreadLocalMap为什么不定义在Thread里面，而是定义在ThreadLocal里面？

因为是线程私有的变量，所以看似定义在Thread里面更加符合逻辑，但是ThreadLocalMap不是必需品，我们并不需要一创建Thread就有ThreadLocalMap，为了减少成本，所以定义在ThreadLocal



#### 二、ThreadLocalRandom

Random计算随机数需要两步，根据老seed计算出新的seed；根据新的seed计算出随机数多线程如果同时获得同样的老seed就会产生同样的随机数，Random采用了CAS来保证只有一个线程获取seed，但是这样自旋会消耗很多性能，ThreadLocalRandom是相当于每个线程维护一个seed变量，不用竞争了在ThreadLocalRandom中并没有存放具体的种子，具体的种子存放在具体的调用线程的threadLocalRandomSeed变量中。和ThreadLocal类似，当线程调用ThreadLocalRandom中的current()方法时，ThreadLocalRandom负责初始化调用线程的threadLocalRandomSeed变量，也就是初始化种子。当调用ThreadLocalRandom的nextInt()方法时，实际是获取当前线程的threadLocalRandomSeed变量作为当前种子来计算新种子，然后将新种子更新到当前线程的threadLocalRandomSeed变量，然后根据这个新种子计算随机数。多个线程通过current获得的ThreadLocalRandom实例，其实获取的是同一个实例instance