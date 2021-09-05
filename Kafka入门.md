---
title: kafka入门介绍
date: 2021-01-08 23:19:47
categories: 消息队列
---

##### 一、消息系统介绍

​	消息系统负责将数据从一个应用程序传输到另一个应用程序，因此应用程序可以专注于数据，但不担心如何共享它。 分布式消息传递基于可靠消息队列的概念。 消息在客户端应用程序和消息传递系统之间异步排队。 有两种类型的消息模式可用 - 一种是点对点，另一种是发布 - 订阅(pub-sub)消息系统。 大多数消息模式遵循 **pub-sub** 

**点对点模式**

​	生产者发送一条消息到queue，一个queue可以有很多消费者，但是一个消息只能被一个消费者接受，当没有消费者可用时，这个消息会被保存直到有 一个可用的消费者，所以Queue实现了一个可靠的负载均衡。

<div>
    <p style="text-align:center">
    <img src="https://yusp-spin.github.io/photo/point.png" width=700 height=400 />
    <br>点对点
    </p>
</div>




**发布订阅模式**

​	在发布 - 订阅系统中，消息被保留在主题中。 与点对点系统不同，消费者可以订阅一个或多个主题并使用该主题中的所有消息。 在发布 - 订阅系统中，消息生产者称为发布者，消息使用者称为订阅者。 

<div>
    <p style="text-align:center">
    <img src="https://yusp-spin.github.io/photo/topic.png" width=700 height=400 />
    <br>发布订阅
    </p>
</div>


##### 二、Kafka

###### 2.1 介绍	

Kafka专为分布式高吞吐量系统而设计。 Kafka往往工作得很好，作为一个更传统的消息代理的替代品。 与其他消息传递系统相比，Kafka具有更好的吞吐量，内置分区，复制和固有的容错能力，这使得它非常适合大规模消息处理应用程序。是一个分布式发布 - 订阅消息系统和一个强大的队列，可以处理大量的数据，并使您能够将消息从一个端点传递到另一个端点。

###### 2.2  Kafka的优势

以下是Kafka的几个好处 -

- **可靠性** - Kafka是分布式，分区，复制和容错的。
- **可扩展性** - Kafka消息传递系统轻松缩放，无需停机。
- **耐用性** - Kafka使用分布式提交日志，这意味着消息会尽可能快地保留在磁盘上，因此它是持久的。
- **性能** - Kafka对于发布和订阅消息都具有高吞吐量。 即使存储了许多TB的消息，它也保持稳定的性能。

Kafka非常快，并保证零停机和零数据丢失。

Kafka可以在许多用例中使用。 其中一些列出如下 -

- **指标** - Kafka通常用于操作监控数据。 这涉及聚合来自分布式应用程序的统计信息，以产生操作数据的集中馈送。
- **日志聚合解决方案** - Kafka可用于跨组织从多个服务收集日志，并使它们以标准格式提供给多个服务器。
- **流处理** - 流行的框架(如Storm和Spark Streaming)从主题中读取数据，对其进行处理，并将处理后的数据写入新主题，供用户和应用程序使用。 Kafka的强耐久性在流处理的上下文中也非常有用。

##### 三、几大MQ比较

<div>
    <p style="text-align:center">
    <img src="https://yusp-spin.github.io/photo/MQ.jpg" width=700 height=1400 />
    <br>几大MQ对比
    </p>
</div>

##### 四、Kafka基础

###### 4.1 几大术语

- **producer和consumer**：消息的发送者叫 Producer，消息的使用者和接受者是 Consumer，生产者将数据保存到 Kafka 集群中，消费者从中获取消息进行业务的处理。

- **broker**：Kafka 集群中有很多台 Server，其中每一台 Server 都可以存储消息，将每一台 Server 称为一个 kafka 实例，也叫做 broker。

- **topic**：一个 topic 里保存的是同一类消息，相当于对消息的分类，每个 producer 将消息发送到 kafka 中，都需要指明要存的 topic 是哪个，也就是指明这个消息属于哪一类。

- **partition**：每个 topic 都可以分成多个 partition，每个 partition 在存储层面是 append log 文件。任何发布到此 partition 的消息都会被直接追加到 log 文件的尾部。为什么要进行分区呢？最根本的原因就是：kafka基于文件进行存储，当文件内容大到一定程度时，很容易达到单个磁盘的上限，因此，采用分区的办法，一个分区对应一个文件，这样就可以将数据分别存储到不同的server上去，另外这样做也可以负载均衡，容纳更多的消费者。

- **Offset**：一个分区对应一个磁盘上的文件，而消息在文件中的位置就称为 offset（偏移量），offset 为一个 long 型数字，它可以唯一标记一条消息。由于kafka 并没有提供其他额外的索引机制来存储 offset，文件只能顺序的读写，所以在kafka中几乎不允许对消息进行“随机读写”。



##### 五、Kafka的安装

​	首先要安装JDK（这个就不说了~）	

​	Apache Kafka 的一个关键依赖是 Apache Zookeeper，它是一个分布式配置和同步服务。Zookeeper 是 Kafka 代理和消费者之间的协调接口。Kafka 服务器通过 Zookeeper 集群共享信息。Kafka 在 Zookeeper 中存储基本元数据，例如关于主题，代理，消费者偏移(队列读取器)等的信息。

​	因此首先需要安装Zookeeper

###### 5.1 Zookeeper安装

安装链接为：http://zookeeper.apache.org/releases.html

下载的是tar.gz

```shell
$ tar -zxf zookeeper-3.4.6.tar.gz
$ cd zookeeper-3.4.6
$ mkdir data
```

创建配置文件

```shell
$ vim conf/zoo.cfg
tickTime=2000
dataDir=/path/to/zookeeper/data
clientPort=2181
initLimit=5
syncLimit=2
```

启动Zookeeper服务器

```shell
$ bin/zkServer.sh start
```



###### 5.2 Kafka安装

下载链接：https://www.apache.org/dyn/closer.cgi?path=/kafka/0.9.0.0/kafka_2.11-0.9.0.0.tgz

解压

```shell
$ cd opt/
$ tar -zxf kafka_2.11.0.9.0.0 tar.gz
$ cd kafka_2.11.0.9.0.0
```

启动Kafka

```shell
$ bin/kafka-server-start.sh config/server.properties
```







***下一节我们将讨论Kafka的架构和基本操作***