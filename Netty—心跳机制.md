---
title: Netty—心跳机制
date: 2020-11-30 11:42:36
categories: Netty
tags: Netty
---

##### 一、Netty心跳机制

​	Netty是由JBOSS提供的一个java开源框架。Netty提供异步的、事件驱动的网络应用程序框架和工具，用以快速开发高性能、高可靠性的网络服务器和客户端程序

​	在Netty中，服务端启动后，等待客户端连接，客户端连接之后，向服务端发送消息。如果客户端在发送，那么服务端必定会收到数据，如果客户端停止发送消息，那么服务端就接收不到这个客户端的消息，既然客户端闲下来了，那么连接资源就是一种浪费，所以服务端检测到一定时间内客户端不活跃的时候，将客户端连接关闭。



##### 二、心跳实现

- 可以使用TCP协议层的Keeplive机制，但是该机制默认的心跳时间是2小时，依赖操作系统实现不够灵活；
- 另一种就是，应用层实现自定义心跳机制，比如Netty实现心跳机制；



##### 三、IdleStateHandler心跳检测

​	pipeline.addLast(new IdleStateHandler(60,45,20, TimeUnit.SECONDS))指的是60s读空闲，45写空闲，20s读写空闲就触发对应userEventTriggered()方法



##### 四、代码实现

**Server.java**

```java
import com.netty.constants.Constants;
import com.netty.factory.ZookeeperFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class NettyServer {

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(parentGroup, childGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)//自己写心跳包
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            //实现心跳机制
                            pipeline.addLast(new IdleStateHandler(60,45,20, TimeUnit.SECONDS));

                            pipeline.addLast(new SimpleServerHandler());
                        }
                    });
            ChannelFuture sync = bootstrap.bind(8088).sync();
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}

```



**ServerHandler.java**

```java
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        ctx.channel().writeAndFlush("is ok\r\n");
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("读空闲==");
                ctx.channel().close();
            }else if(event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("写空闲==");
            }else if(event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("读写空闲==");
                ctx.channel().writeAndFlush("ping\r\n");
            }
        }
    }
}
```





**Client.java**

```java
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

public class NettyClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8088;
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new SimpleClientHandler());
                        }
                    });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().writeAndFlush("hello, server");
            f.channel().writeAndFlush("/r/n");
            f.channel().closeFuture().sync();
            Object result = f.channel().attr(AttributeKey.valueOf("ssssss")).get();
            System.out.println("获取到服务器返回的数据：" + result);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}

```





**ClientHandler.java**

```java
package com.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if("ping".equals(msg.toString())) {
            ctx.channel().writeAndFlush("pong\r\n");
            return;
        }
        System.out.println(msg.toString());
    }
```



##### 五、总结

​	使用 Netty 实现心跳机制的关键就是利用 IdleStateHandler 来产生对应的 idle 事件.

​	对应userEventTriggered()方法来触发对应的idle事件处理