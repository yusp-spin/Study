---
title: pagehelper的使用方法
date: 2020-11-27 10:55:54
categories:  框架
---

##### 一、介绍

现在一般springboot，mybatis项目中的mapper，pojo都是通过逆向工程直接生成的，但是生成的mapper中的查询语句中是没有分页功能的，如果想要分页的话，只能自己再去xml配置文件中修改sql语句，显得特别麻烦。为了方便快捷的翻页，这里引入一个分页插件。

pagehelper，原理如下

![fenye](../photo/fenye.png)



使用这个插件后，会自动在我们的sql查询中加上limit，而不需要我们自己再去做limit处理



##### 二、使用方法

​	首先引入依赖

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.11</version>
</dependency>
```

​	然后再在yml文件中配置

```yml
#pagehelper配置
pagehelper:
  reasonable: true
  support-methods-arguments: true
  params: count=countSql
```

使用的时候只需要在我们的查询使用之前加上一句就行

​	PageHelper.startPage(pageNum,pageSize);

​	pageNum是显示第几页；pageSize是每页的记录数

```java
@ResponseBody
    @RequestMapping("/list")
    public List<TbItem> selectAll() {
        PageHelper.startPage(1,10);
        List<TbItem> tbItems = tbItemService.selectAll();
        return tbItems;
    }
```

注意：查询语句必须紧跟在PageHelper.startPage(pageNum,pageSize);语句之后，不然分页会有问题。

为了方便，我们通常创建一个PageInfo的对象，从对象中获取分页信息

```java
@ResponseBody
    @RequestMapping("/list")
    public PageInfo<TbItem> selectAll() {
        PageHelper.startPage(1,10);
        List<TbItem> tbItems = tbItemService.selectAll();
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        return pageInfo;
    }
```

