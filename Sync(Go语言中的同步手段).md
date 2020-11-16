---
title: Sync(Go语言中的同步手段)
date: 2020-11-6 11:23:47
categories: 并发编程（go）
---

##### 1.sync.WaitGroup

​	Go语言中可以使用sync.WaitGroup来实现并发任务的同步。

​	sync.WaitGroup内部维护着一个计数器，计数器的值可以增加和减少。例如当我们启动了N 个并发任务时，就将计数器值增加N。每个任务完成时通过调用Done()方法将计数器减1。通过调用Wait()来等待并发任务执行完，当计数器值为0时，表示所有并发任务已经完成

 	sync.WaitGroup有以下几个方法：

| 方法名                          | 功能                |
| ------------------------------- | ------------------- |
| (wg * WaitGroup) Add(delta int) | 计数器+delta        |
| (wg *WaitGroup) Done()          | 计数器-1            |
| (wg *WaitGroup) Wait()          | 阻塞直到计数器变为0 |



```go
package main

import (
	"fmt"
	"sync"
)

var wg sync.WaitGroup

func hello()  {
	defer wg.Done()
	fmt.Println("hello Goroutine!")
}

func main() {
	wg.Add(1)
	go hello()
	fmt.Println("main gorountine done")
	wg.Wait()
}
```





##### 2.sync.Once

​	确保某些操作在高并发的场景下只执行一次,sunc.Once是一个针对值执行一次场景的解决方案

​	整个程序，只会执行printNum()方法一次,print()方法是不会被执行的。

```go
package main

import (
	"fmt"
	"sync"
	"time"
)

var once sync.Once

func main() {
	for i, v :=range make([]string, 10) {
		once.Do(printNum)
		fmt.Println("count:",v,"---",i)
	}

	for i := 0; i < 10; i++ {
		go func() {
			once.Do(print)
			fmt.Println("hello")
		}()
	}

	time.Sleep(4000)
}

func printNum()  {
	fmt.Println("123456")
}

func print()  {
	fmt.Println("abcdefg")
}
```



##### 3.sync.Map

​	Go语言中内置的map不是并发安全的,有些场景下就需要为map加锁来保证并发的安全性了，Go语言的sync包中提供了一个开箱即用的并发安全版map–sync.Map。开箱即用表示不用像内置的map一样使用make函数初始化就能直接使用。同时sync.Map内置了诸如Store、Load、LoadOrStore、Delete、Range等操作方法