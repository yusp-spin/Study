---
title: AQS介绍
date: 2021-03-29 14:59:23
categories: 同步
---

##### 一、AQS的简单介绍

​	 AQS的全称是AbstractQueuedSynchronizer ，这个类在 java.util.concurrent.locks 包下面 AQS是基于FIFO的队列实现的，并且内部维护了一个状态变量state，通过原子更新这个状态变量state即可以实现加锁解锁操作。 ReentrantLock，Semaphore，其他的如  CyclicBarrier  ，CountDownLatch，FutureTask SynchronousQueue等等皆是基于 AQS 的。 

​	当然，我们自己也能利用 AQS 非常轻松容易地构造出符合我们自己需求的同步器。	

##### 二、源码介绍

1. 先看内部类，节点中保存着当前线程、前一个节点、后一个节点以及线程的状态等信息。

```java
static final class Node {
    // 共享模式
    static final Node SHARED = new Node();
    // 互斥模式
    static final Node EXCLUSIVE = null;

    // 标识线程已取消
    static final int CANCELLED =  1;
    // 标识后继节点需要唤醒
    static final int SIGNAL    = -1;
    // 标识线程等待在一个条件上
    static final int CONDITION = -2;
    // 标识后面的共享锁需要无条件的传播（共享锁需要连续唤醒读的线程）
    static final int PROPAGATE = -3;
    
    // 当前节点保存的线程对应的等待状态
    volatile int waitStatus;

    // 前一个节点
    volatile Node prev;
    
    // 后一个节点
    volatile Node next;

    // 当前节点保存的线程
    volatile Thread thread;

    // 下一个等待在条件上的节点（Condition锁时使用）
    Node nextWaiter;

    // 是否是共享模式
    final boolean isShared() {
        return nextWaiter == SHARED;
    }

    // 获取前一个节点
    final Node predecessor() throws NullPointerException {
        Node p = prev;
        if (p == null)
            throw new NullPointerException();
        else
            return p;
    }

    // 节点的构造方法
    Node() {    // Used to establish initial head or SHARED marker
    }

    // 节点的构造方法
    Node(Thread thread, Node mode) {     // Used by addWaiter
        // 把共享模式还是互斥模式存储到nextWaiter这个字段里面了
        this.nextWaiter = mode;
        this.thread = thread;
    }

    // 节点的构造方法
    Node(Thread thread, int waitStatus) { // Used by Condition
        // 等待的状态，在Condition中使用
        this.waitStatus = waitStatus;
        this.thread = thread;
    }
}
```


2. 关键的state

```java
*// 控制加锁解锁的状态变量* private volatile int state; 
```

​	 用volatile关键字来修饰，因为是在多线程环境下操作，要保证它们的值修改之后对其它线程立即可见 

state非常重要，所有的同步器都是通过stae来控制锁的状态。state的修改必须用cas

​	例如：CountDownLatch，首先通过构造函数设置state = n，需要countDown()执行n次，await()才会返回。这里用到的就是state，每当执行一次countDown()，state就-1，知道所有的子线程执行完毕，state为0，await()方法就可以返回

3. 实现一个AQS所需要实现的方法

```java
// 互斥模式下尝试获取锁
protected boolean tryAcquire(int arg) {
    throw new UnsupportedOperationException();
}
// 互斥模式下尝试释放锁
protected boolean tryRelease(int arg) {
    throw new UnsupportedOperationException();
}
// 共享模式下尝试获取锁
protected int tryAcquireShared(int arg) {
    throw new UnsupportedOperationException();
}
// 共享模式下尝试释放锁
protected boolean tryReleaseShared(int arg) {
    throw new UnsupportedOperationException();
}
// 如果当前线程独占着锁，返回true
protected boolean isHeldExclusively() {
    throw new UnsupportedOperationException();
}

```

这里用到了一种设计模式，即模板方法模式，自定义同步器时只需要重写上面几个方法即可，AQS中其他类都是final类型的，只有这几个方法能被其它类使用。那么重写了几个方法为什么可以实现同步器呢？这是因为AQS父类已经帮我买写好了一系列操作，包括入队，出队等。具体操作等后面介绍具体同步器的时候就可以知道。后面会介绍ReentrantLock，CountDownLatch等。



##### 三、核心思想

​	AQS核心思想是：如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效 的工作线程，并且将共享资源设置为锁定状态。如果被请求的共享资源被占用，那么就需 要一套线程阻塞等待以及被唤醒时锁分配的机制，这个机制 AQS 是用 CLH 队列锁实现 的，即将暂时获取不到锁的线程加入到队列中