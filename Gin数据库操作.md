---
title: Gin数据库操作
date: 2020-11-1 22:15:47
categories: Gin框架
---

##### 一、连接数据库

​	准备好数据库，表，同时与Gin建立连接

```go
//初始化数据库连接
func InitDatabase() (*sql.DB, error) {
	//将数据转换成数据库url作为返回值
	url := strings.Join([]string{"root", ":", "123456", "@tcp(", "127.0.0.1", ":", "3306", ")/", "gomysql"}, "")
	db, err := sql.Open("mysql", url)
	if err != nil {
		log.Printf("open database error:%v", err)
		return nil, err
		}
	return db, nil
}
```

​	新建一个全局变量`sql.DB`方便我们后期调用，然后通过 `sql.Open`对数据进行连接



##### 二、增删改

​	往数据库中增删改

```go
//用于增删改
 //执行增、改、删任务
 func Execute(db *sql.DB, sql string, params ...interface{}) error {
	stmt, _ := db.Prepare(sql) //预编译
	defer stmt.Close()
	_, err := stmt.Exec(params...)
	if err != nil {
		log.Printf("execute sql error:%v\n", err)
		return err
		}
	log.Println("execute sql success")
	return nil
 }
```



##### 三、查询

```go
//查询数据库数据
func QueryData(db *sql.DB, sql string, params ...interface{}) (*sql.Rows, error) {
	stmt, _ := db.Prepare(sql)
	defer stmt.Close()
	rows, err := stmt.Query(params...)
	if err != nil {
		log.Printf("query data error:%v", err)
		return nil, err
		}
	log.Println("query data success")
	return rows, nil
}
```



##### 四、总结

```go
package main

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"log"
	"strings"
)

type Student struct {
	id int
	name string
	age int
}

func main() {
	db, err := InitDatabase()
	defer db.Close()
	if err != nil {
		log.Println(err)
		return
	}

	//增加
	insert := "insert into person (name, age) values (?,?)"
	err = Execute(db, insert, "xiaoming", 23)
	if err != nil {
		log.Println("insert data error : %v\n", err)
		return
	}

	querySql := "select * from person"
	data, err := QueryData(db,querySql)
	defer data.Close()
	if err != nil {
		log.Printf("query data error:%v\n", err)
		return
	}
	s := new(Student)
	for data.Next() {
		data.Scan(&s.id, &s.name, &s.age)
		log.Println(*s)
	}
}


//初始化数据库连接
func InitDatabase() (*sql.DB, error) {
	//将数据转换成数据库url作为返回值
	url := strings.Join([]string{"root", ":", "123456", "@tcp(", "127.0.0.1", ":", "3306", ")/", "gomysql"}, "")
	db, err := sql.Open("mysql", url)
	if err != nil {
		log.Printf("open database error:%v", err)
		return nil, err
		}
	return db, nil
}

//用于增删改
 //执行增、改、删任务
 func Execute(db *sql.DB, sql string, params ...interface{}) error {
	stmt, _ := db.Prepare(sql) //预编译
	defer stmt.Close()
	_, err := stmt.Exec(params...)
	if err != nil {
		log.Printf("execute sql error:%v\n", err)
		return err
		}
	log.Println("execute sql success")
	return nil
 }

//查询数据库数据
func QueryData(db *sql.DB, sql string, params ...interface{}) (*sql.Rows, error) {
	stmt, _ := db.Prepare(sql)
	defer stmt.Close()
	rows, err := stmt.Query(params...)
	if err != nil {
		log.Printf("query data error:%v", err)
		return nil, err
		}
	log.Println("query data success")
	return rows, nil
}
```

