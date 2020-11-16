---
title: Gin框架入门
date: 2020-11-1 10:12:47
categories: Gin框架
---

##### 一、Gin简介

​	Gin 是使用 Go/golang 语言实现的 HTTP Web 框架。接口简洁，性能极高，框架源码仅5K

##### 二、Gin 特性

- **快速**：路由不使用反射，基于Radix树，内存占用少。
- **中间件**：HTTP请求，可先经过一系列中间件处理，例如：Logger，Authorization，GZIP等。这个特性和 NodeJs 的 `Koa` 框架很像。中间件机制也极大地提高了框架的可扩展性。
- **异常处理**：服务始终可用，不会宕机。Gin 可以捕获 panic，并恢复。而且有极为便利的机制处理HTTP请求过程中发生的错误。
- **JSON**：Gin可以解析并验证请求的JSON。这个特性对`Restful API`的开发尤其有用。
- **路由分组**：例如将需要授权和不需要授权的API分组，不同版本的API分组。而且分组可嵌套，且性能不受影响。
- **渲染内置**：原生支持JSON，XML和HTML的渲染。

```go
package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func main() {
	r := gin.Default()
	r.GET("/", func(c *gin.Context) {
		c.String(http.StatusOK, "hello, world")
	})
    r.Run(":8000")
}
```



1. 首先，我们使用了`gin.Default()`生成了一个实例，这个实例即 WSGI 应用程序。
2. 接下来，我们使用`r.Get("/", ...)`声明了一个路由，告诉 Gin 什么样的URL 能触发传入的函数，这个函数返回我们想要显示在用户浏览器中的信息。
3. 最后用 `r.Run()`函数来让应用运行在本地服务器上，默认监听端口是 _8080_，可以传入参数设置端口，例如`r.Run(":8000")`即运行在8000端口。

##### 三、路由（Route）

​	1. 路由方法有 **GET, POST, PUT, PATCH, DELETE** 和 **OPTIONS**，还有**Any**，可匹配以上任意类型的请求。

​	2.解析路径参数；动态的路由，如 `/user/:name`，通过调用不同的 url 来传入不同的 name。`/user/:name/*role`，`*` 代表可选

```go
package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func main() {
	r := gin.Default()
	r.GET("/user/:name", func(c *gin.Context) {
		name := c.Param("name")
		c.String(http.StatusOK, "hello,%s", name)
	})
	r.Run(":8000")
}
```

​	3.获取Query参数

```go
package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

// 匹配users?name=xxx
func main() {
	r := gin.Default()
	r.GET("/user", func(c *gin.Context) {
		name := c.Query("name")
		c.String(http.StatusOK, "hello, %s", name)
	})
	r.Run(":8000")
}
```

​	4.获取POST参数

```go
package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func main() {
	r := gin.Default()
	r.POST("/form", func(c *gin.Context) {
		username := c.PostForm("username")
		password := c.DefaultPostForm("password", "000000") // 可设置默认值

		c.JSON(http.StatusOK, gin.H{
			"username": username,
			"password": password,
		})
	})
	r.Run(":8000")
}
```



##### 四、HTML模板

```go
package main

import (
    "github.com/gin-gonic/gin"
    "net/http"
)

func main()  {
    r := gin.Default()
    // 指明html加载文件目录
    r.LoadHTMLGlob("./html/*")
    r.Handle("GET", "/", func(c *gin.Context) {
        // 返回HTML文件，响应状态码200，html文件名为index.html，模板参数为nil
        c.HTML(http.StatusOK, "index.html", nil)
    })
    r.Run()
}
```

