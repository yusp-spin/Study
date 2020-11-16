---
title: Netty介绍
date: 2020-11-9 22:12:47
categories: Netty
tags: Netty
---

##### 一、什么是Netty

​	Netty是由JBOSS提供的一个java开源框架。Netty提供异步的、事件驱动的网络应用程序框架和工具，用以快速开发高性能、高可靠性的网络服务器和客户端程序

​	也就是说，Netty 是一个基于NIO的客户、服务器端编程框架，使用Netty 可以确保你快速和简单的开发出一个网络应用，例如实现了某种协议的客户，服务端应用。Netty相当简化和流线化了网络应用的编程开发过程，例如，TCP和UDP的socket服务开发。

我们下面编写四个类

1.用于接收数据的服务器端Socket

2.用于接收客户端的消息，用于接收和反馈客户端发出的消息类ServertHandler

3.用于发送数据的服务器端Client

4.用于发送数据和接收服务器端发出的数据处理类ClientHandler



**Socket.java**

```java
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
 
public class Server {
	
	public static void main(String[] args) throws InterruptedException {
		//1.第一个线程组是用于接收Client端连接的
		EventLoopGroup bossGroup = new NioEventLoopGroup(); 
		//2.第二个线程组是用于实际的业务处理的
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup);//绑定两个线程池
		b.channel(NioServerSocketChannel.class);//指定NIO的模式，如果是客户端就是NioSocketChannel
		b.option(ChannelOption.SO_BACKLOG, 1024);//TCP的缓冲区设置
		b.option(ChannelOption.SO_SNDBUF, 32*1024);//设置发送缓冲的大小
		b.option(ChannelOption.SO_RCVBUF, 32*1024);//设置接收缓冲区大小
		b.option(ChannelOption.SO_KEEPALIVE, true);//保持连续
		b.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());//拆包粘包定义结束字符串（第一种解决方案）
				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf));//在管道中加入结束字符串
			//	sc.pipeline().addLast(new FixedLengthFrameDecoder(200));第二种定长
				sc.pipeline().addLast(new StringDecoder());//定义接收类型为字符串把ByteBuf转成String
				sc.pipeline().addLast(new ServertHandler());//在这里配置具体数据接收方法的处理
			}
		});
		ChannelFuture future = b.bind(8765).sync();//绑定端口
		future.channel().closeFuture().sync();//等待关闭(程序阻塞在这里等待客户端请求)
		bossGroup.shutdownGracefully();//关闭线程
		workerGroup.shutdownGracefully();//关闭线程
	}
}
```

1.在上面这个Server.java中，我们都要定义两个线程池，boss和worker，boss是用于管理连接到server端的client的连接数的线程池，而woeker是用于管理实际操作的线程池。

2.ServerBootstrap用一个ServerSocketChannelFactory 来实例化。ServerSocketChannelFactory 有两种选择，一种是NioServerSocketChannelFactory，一种是OioServerSocketChannelFactory。 前者使用NIO，后则使用普通的阻塞式IO。它们都需要两个线程池实例作为参数来初始化，一个是boss线程池，一个是worker线程池。

3.然后使ServerBookstrap管理boss和worker线程池。并且设置各个缓冲区的大小。

4.这里的事件处理类经常会被用来处理一个最近的已经接收的Channel。ChannelInitializer是一个特殊的处理类，他的目的是帮助使用者配置一个新的Channel。也许你想通过增加一些处理类比如NettyServerHandler来配置一个新的Channel 或者其对应的ChannelPipeline来实现你的网络程序。 当你的程序变的复杂时，可能你会增加更多的处理类到pipline上，然后提取这些匿名类到最顶层的类上。

5.在使用原始的encoder、decoder的情况下，Netty发送接收数据都是按照ByteBuf的形式，其它形式都是不合法的。 而在上面这个Socket中，我使用sc.pipeline().addLast()这个方法设置了接收为字符串类型，注意：只能设置接收为字符串类型，发送还是需要发送ByteBuf类型的数据。而且在这里我还设置了以$_为结尾的字符串就代表了本次请求字符串的结束。

6.通过b.bind绑定端口，用于监听的端口号。



**ServerHandler.java**

```java
public class ServertHandler extends ChannelHandlerAdapter {
 
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("server"+body);//前面已经定义了接收为字符串，这里直接接收字符串就可以
		//服务端给客户端的响应
		String response= " hi client!$_";//发送的数据以定义结束的字符串结尾
		ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));//发送必须还是ByteBuf类型
	}
 
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		 cause.printStackTrace();
		  ctx.close();
	}	
}
```



ServertHandler继承自 ChannelHandlerAdapter，这个类实现了ChannelHandler接口，ChannelHandler提供了许多事件处理的接口方法，然后你可以覆盖这些方法。现在仅仅只需要继承ChannelHandlerAdapter类而不是你自己去实现接口方法。

1.由于我们再server端开始的时候已经定义了接收类型为String，所以在这里我们接收到的msg直接强转成String就可以了。同时也要定义以什么为一次请求的结尾。

**Client.java**

```java
public class Client {
 
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup worker = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(worker)
		.channel(NioSocketChannel.class)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes()); 
				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,buf));
				sc.pipeline().addLast(new StringDecoder());
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		ChannelFuture f=b.connect("127.0.0.1",8765).sync();
		f.channel().writeAndFlush(Unpooled.copiedBuffer(" hi server2$_".getBytes()));
		f.channel().writeAndFlush(Unpooled.copiedBuffer(" hi server3$_".getBytes()));
		f.channel().writeAndFlush(Unpooled.copiedBuffer(" hi server4$_".getBytes()));
		f.channel().closeFuture().sync();
		worker.shutdownGracefully();
	}
}
```

client端和Socket端几乎代码相同，只是client端用的不是ServerBootstrap而是Bootstrap来管理连接



**ClientHandler.java**

```java

public class ClientHandler extends ChannelHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			System.out.println("client"+msg.toString());
		} finally {
			ReferenceCountUtil.release(msg);//释放缓冲区
		}
	}
 
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		 cause.printStackTrace();
		  ctx.close();
	}
}
```

ClientHandler和ServertHandler代码和原理也是一样，只是在client端我们要释放缓冲区。为什么在ServerHandler我们不需要释放呢 ？因为在ServertHandler我们调用ctx.writeAndFlush方法的时候，这个方法默认已经帮我们释放了缓冲区。



参考：https://blog.csdn.net/a347911/article/details/53734255