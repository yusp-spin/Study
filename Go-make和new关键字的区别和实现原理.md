---
title: Go-make和new关键字的区别和实现原理
date: 2020-11-8 21:32:47
categories: Go基础
---

##### 一、简介

​	 make 和 new都用来初始化一个结构，make 关键字的主要作用是初始化内置的数据结构，也就是我们在前面提到的数组、切片和 Channel，而当我们想要获取指向某个类型的指针时可以使用 new 关键字



##### 二、区别

make在Go语言中用来初始化语言的基本类型

```go
func main() {
	slice := make([]int, 0, 100)
	myMap := make(map[string]int, 10)
	ch := make(chan int, 5)
}
```

 new 的作用其实只是接收一个类型作为参数然后返回一个指向这个类型的指针

```go
i := new(int)
var v int
i := &v
```

所以：make 用于创建切片、哈希表和管道等内置数据结构，new 用于分配并创建一个指向对应类型的指针。



##### 三、原理

**make**

​	在编译期间的类型检查阶段，Go语言其实就将代表 make 关键字的 OMAKE 节点根据参数类型的不同转换成了 OMAKESLICE、OMAKEMAP 和 OMAKECHAN 三种不同类型的节点，这些节点最终也会调用不同的运行时函数来初始化数据结构。

**new**

​	内置函数 new 会在编译期间的 SSA 代码生成阶段经过 callnew 函数的处理，如果请求创建的类型大小是 0，那么就会返回一个表示空指针的 zerobase 变量，在遇到其他情况时会将关键字转换成 newobject：

```go
func callnew(t *types.Type) *Node {
    if t.NotInHeap() {
        yyerror("%v is go:notinheap; heap allocation disallowed", t)
    }
    dowidth(t)
 
    if t.Size() == 0 {
        z := newname(Runtimepkg.Lookup("zerobase"))
        z.SetClass(PEXTERN)
        z.Type = t
        return typecheck(nod(OADDR, z, nil), ctxExpr)
    }
 
    fn := syslook("newobject")
    fn = substArgTypes(fn, t)
    v := mkcall1(fn, types.NewPtr(t), nil, typename(t))
    v.SetNonNil(true)
    return v
}
```



​	需要提到的是，哪怕当前变量是使用 var 进行初始化，在这一阶段也可能会被转换成 newobject 的函数调用并在堆上申请内存：

```go
func walkstmt(n *Node) *Node {
    switch n.Op {
    case ODCL:
        v := n.Left
        if v.Class() == PAUTOHEAP {
            if prealloc[v] == nil {
                prealloc[v] = callnew(v.Type)
            }
            nn := nod(OAS, v.Name.Param.Heapaddr, prealloc[v])
            nn.SetColas(true)
            nn = typecheck(nn, ctxStmt)
            return walkstmt(nn)
        }
    case ONEW:
        if n.Esc == EscNone {
            r := temp(n.Type.Elem())
            r = nod(OAS, r, nil)
            r = typecheck(r, ctxStmt)
            init.Append(r)
            r = nod(OADDR, r.Left, nil)
            r = typecheck(r, ctxExpr)
            n = r
        } else {
            n = callnew(n.Type.Elem())
        }
    }
}
```



  当然这也不是绝对的，如果当前声明的变量或者参数不需要在当前作用域外生存，那么其实就不会被初始化在堆上，而是会初始化在当前函数的栈中并随着函数调用的结束而被销毁。

​	newobject 函数的工作就是获取传入类型的大小并调用 mallocgc 在堆上申请一片大小合适的内存空间并返回指向这片内存空间的指针：  

```go
func newobject(typ *_type) unsafe.Pointer {
    return mallocgc(typ.size, typ, true)
}
```



##### 四、总结

​	简单总结一下Go语言中 make 和 new 关键字的实现原理，make 关键字的主要作用是创建切片、哈希表和 Channel 等内置的数据结构，而 new 的主要作用是为类型申请一片内存空间，并返回指向这片内存的指针



参考：https://blog.csdn.net/qq_32252917/article/details/102953438