---
title: SpringBoot 必须依赖spring-boot-parent？
date: 2020-11-21 21:27:47
categories: SpringBoot 框架
---

##### 在做项目的时候遇到一个问题

##### 问题

做项目的时候遇到一个问题

子moudle的pom中已经引用了<parent>为父moudle,但是springboot项目又需要引用
spring-boot-start-parent，但是同时引用两个<parent>的话pom文件会报错，于是我们

翻看了官方文档已经查阅资料发现，可以不用<parent>,直接引包亦可以实现同样的功能，SpringBoot 不是必须依赖spring-boot-parent。

```xml
<dependencyManagement>
        <dependencies>
            <dependency>
                <!-- Import dependency management from Spring Boot -->
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.4.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

