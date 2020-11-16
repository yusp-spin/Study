---
title: Unsafe函数六道面试题
date: 2020-09-14 21:22:47
categories: 并发编程（java）
tags: 并发
---

1. Unsafe是什么？
	Java 无法直接访问底层操作系统，而是通过本地（native）方法来访问。不过尽管如此，JVM 还是开了一个后门，JDK 中有一个类 Unsafe，底层是使用C/C++写的，它提供了硬件级别的原子操作。Unsafe为我们提供了访问底层的机制，这种机制仅供java核心类库使用，而不应该被普通用户使用。
	
	UnSafe的功能主要有：（1）实例化一个类；（2）修改私有字段的值；（3）抛出checked异常；（4）使用堆外内存；（5）CAS操作；（6）阻塞/唤醒线程；（7）内存屏障
	
2. Unsafe为什么是不安全的？
	因为官方不推荐使用,因为不安全,例如你使用unsafe创建一个超级大的数组,但是这个数组jvm是不管理的,只能你自己操作,容易oom,也不利于资源的回收.

3. Unsafe的实例怎么获取？
	a. 在jdk8和之前如果获得其单例对象是会抛出异常的，只能通过反射获取，在jdk9及以后，可以通过getUnsafe静态方法获取
	b. 我们知道 unsafe是提供给java核心内库使用的，那么我们如何获取Unsafe的实例呢？当然是反射！
	c. 代码：
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		Unsafe unsafe = (Unsafe) f.get(null);
4. 讲一讲Unsafe中的CAS操作？
	a. JUC中用到了大量的CAS，他们的底层其实都是采用Unsafe的CAS操作，
	b. CAS（比较与交换，Compare and swap）是一种有名的无锁算法,因为不需要加锁，性能比加锁搞。CAS是一个CPU指令。CAS还是一个乐观锁技术
	c. CAS存在的问题：
		i. 经典的ABA问题，危害有（以栈举例），解决方案：版本号控制，有的数据结构在高位用邮戳标记；不重复使用节点引用，而是构建新的节点，
		ii. CAS常常搭配自旋一起使用，如果自选长时间不成功，循环时间长 开销大
		iii. 只能保持一个共享变量的安全操作

5. Unsafe的阻塞/唤醒操作？
	a. LockSupport类中的park与unpark方法对unsafe中的park与unpark方法做了封装，LockSupport类中有各种版本pack方法，但最终都调用了Unsafe.park()方法。

6. 实例化类的六种方式？
	a. 通过构造方法new一个对象
	b. 通过Class实例一个类
	c. 通过发射实例化一个雷
	d. 通过克隆
	e. 通过反序列化
	f. 通过Unsafe实例化一个类