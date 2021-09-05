---
title: 理解Netty中的零拷贝(Zero-Copy)
date: 2021-09-5 17:26:01
categories: Netty
tags: Netty
---

##### 一、理解零拷贝

​	*"Zero-copy" describes computer operations in which the CPU does not perform the task of copying data from one memory area to another.*

​	“零拷贝”是指计算机操作的过程中，CPU不需要为数据在内存之间的拷贝消耗资源。而它通常是指计算机在网络上发送文件时，不需要将文件内容拷贝到用户空间（User Space）而直接在内核空间（Kernel Space）中传输到网络的方式。

<div>
    <p style="text-align:center">
    <img src="https://yusp-spin.github.io/photo/Zero-Copy.png" width=700 height=400 />
    <br>零拷贝
    </p>
</div>



Zero Copy的模式中，避免了数据在用户空间和内存空间之间的拷贝，从而提高了系统的整体性能。Linux中的`sendfile()`以及Java NIO中的`FileChannel.transferTo()`方法都实现了零拷贝的功能，而在Netty中也通过在`FileRegion`中包装了NIO的`FileChannel.transferTo()`方法实现了零拷贝。



##### 二、Netty中的零拷贝

在Netty中还有另一种形式的零拷贝，即Netty允许我们将多段数据合并为一整段虚拟数据供用户使用，而过程中不需要对数据进行拷贝操作，这也是我们今天要讲的重点。我们都知道在stream-based transport（如TCP/IP）的传输过程中，数据包有可能会被重新封装在不同的数据包中，即 粘包

粘包怎么出现的呢?

我们知道TCP/IP协议是目前的主流网络协议。它是一个多层协议，最下层是物理层，最上层是应用层(HTTP协议等)，而做Java应用开发，一般只接触TCP以上，即传输层和应用层的内容。这也是Netty的主要应用场景。

TCP报文有个比较大的特点，就是它传输的时候，会先把应用层的数据项拆开成字节，然后按照自己的传输需要，选择合适数量的字节进行传输。什么叫"自己的传输需要"？首先TCP包有最大长度限制，那么太大的数据项肯定是要拆开的。其次因为TCP以及下层协议会附加一些协议头信息，如果数据项太小，那么可能报文大部分都是没有价值的头信息，这样传输是很不划算的。因此有了收集一定数量的小数据，并打包传输的Nagle算法(这个东东在HTTP协议里会很讨厌，Netty里可以用setOption("tcpNoDelay", true)关掉它)。

这么说可能不太好理解，我们举个例子吧：

发送时，我们这样分3次写入('|'表示两个buffer的分隔):

```
   +-----+-----+-----+
   | ABC | DEF | GHI |
   +-----+-----+-----+
```

接收时，可能变成了这样:

```
   +----+-------+---+---+
   | AB | CDEFG | H | I |
   +----+-------+---+---+
```

因此在实际应用中，很有可能一条完整的消息被分割为多个数据包进行网络传输，而单个的数据包对你而言是没有意义的，只有当这些数据包组成一条完整的消息时你才能做出正确的处理，而Netty可以通过零拷贝的方式将这些数据包组合成一条完整的消息供你来使用。而此时，零拷贝的作用范围仅在用户空间中。



##### 三、 Netty中的ChannelBuffer

与Zero Copy直接相关的`CompositeChannelBuffer`类。 ###CompositeChannelBuffer类 `CompositeChannelBuffer`类的作用是将多个`ChannelBuffer`组成一个虚拟的`ChannelBuffer`来进行操作。为什么说是虚拟的呢，因为`CompositeChannelBuffer`并没有将多个`ChannelBuffer`真正的组合起来，而只是保存了他们的引用，这样就避免了数据的拷贝，实现了Zero Copy。

```java
	public class CompositeChannelBuffer{

	    //components保存所有内部ChannelBuffer
	    private ChannelBuffer[] components;
	    //indices记录在整个CompositeChannelBuffer中，每个components的起始位置
	    private int[] indices;
	    //缓存上一次读写的componentId
	    private int lastAccessedComponentId;

	    public byte getByte(int index) {
	        //通过indices中记录的位置索引到对应第几个子Buffer
	        int componentId = componentId(index);
	        return components[componentId].getByte(index - indices[componentId]);
	    }

	    public void setByte(int index, int value) {
	        int componentId = componentId(index);
	        components[componentId].setByte(index - indices[componentId], value);
	    }

	}		
```



其中`readerIndex`既读指针和`writerIndex`既写指针是从`AbstractChannelBuffer`继承而来的；然后`components`是一个`ChannelBuffer`的数组，他保存了组成这个虚拟Buffer的所有子Buffer，`indices`是一个`int`类型的数组，它保存的是各个Buffer的索引值；最后的`lastAccessedComponentId`是一个`int`值，它记录了最后一次访问时的子Buffer ID。从这个数据结构，我们不难发现所谓的`CompositeChannelBuffer`实际上就是将一系列的Buffer通过数组保存起来，然后实现了`ChannelBuffer` 的接口，使得在上层看来，操作这些Buffer就像是操作一个单独的Buffer一样。



完全理解Netty零拷贝

参考：

https://my.oschina.net/plucury/blog/192577

https://github.com/code4craft/netty-learning/blob/master/posts/ch2-buffer.md