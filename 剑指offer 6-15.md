---
title: 剑指offer 6-15
date: 2020-10-05 9:48:47
categories: 算法
tags: 剑指offer
---

###### 6.  矩形覆盖

​	我们可以用21的小矩形横着或者竖着去覆盖更大的矩形。请问用n个21的小矩形无重叠地覆盖一个2n的大矩形，总共有多少种方法？

```java
public class Solution {
    public int RectCover(int target) {
        if(target <= 2) {
            return  target;
        }
        return RectCover(target - 1) + RectCover(target - 2);
    }
}
```


###### 7. 斐波那契数列

​	大家都知道斐波那契数列，现在要求输入一个整数n，请你输出斐波那契数列的第n项（从0开始，第0项为0，第1项是1）。n<=39

```java
public class Solution {
    public int Fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return Fibonacci(n - 1) + Fibonacci(n - 2);
    }
}
```



###### 8. 跳台阶

​	一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法（先后次序不同算不同的结果）

```java
public class Solution {
    public int JumpFloor(int target) {
        if(target <= 2) {
            return  target;
        }
        return JumpFloor(target - 1) + JumpFloor(target - 2);
    }
}
```



###### 9.变态跳台阶

​	一只青蛙一次可以跳上1级台阶，也可以跳上2级……它也可以跳上n级。求该青蛙跳上一个n级的台阶总共有多少种跳法。

```java
public class Solution {
    public int JumpFloorII(int target) {
        if (target <= 2){
            return target;
        }
        return 2 * JumpFloorII(target - 1);
    }
}
```



###### 10.矩形覆盖

​	我们可以用21的小矩形横着或者竖着去覆盖更大的矩形。请问用n个21的小矩形无重叠地覆盖一个2n的大矩形，总共有多少种方法？

```java
public class Solution {
    public int RectCover(int target) {
        if (target <= 2) {
            return  target;
        }
        return RectCover(target - 1) + RectCover(target - 2);
         
    }
}
```



###### 11.二进制中1的个数

输入一个整数，输出该数32位二进制表示中1的个数。其中负数用补码表示。

```java
public class Solution {
    public int NumberOf1(int n) {
        int count = 0;
        while(n != 0) {
            n = n &(n-1);
            count++;
        }
        return count;
    }
}
```



###### 12.数值的整数次方

​	给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。保证base和exponent不同时为0

```java
public class Solution {
    public double Power(double base, int exponent) {
        if(base == 0.0) {
            return 0.0;
        }
        if(exponent == 0) {
            return 1.0;
        }
        if(exponent < 0) {
            exponent = -exponent;
            base = 1 / base;
        }
        return fastpow(base, exponent);
  }
     
    private double fastpow(double base,int exponent) {
        if(exponent == 0){
            return 1;
        }
        double half = fastpow(base, exponent / 2);
        if (exponent % 2 == 0) {
            return half * half;
        } else {
            return half * half * base;
        }
    }
}
```



###### 13.调整数组顺序使奇数位于偶数前面

​	输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，所有的偶数位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。

```java
public class Solution {
    public void reOrderArray(int [] array) {
        int i = 0;
        while (i < array.length) {
            while (array[i] % 2 == 1) {
                i++;
                if (i == array.length) {
                    return;
                }
            }
            int j = i;
            while (array[j] % 2 == 0) {
                j++;
                if(j == array.length) {
                    return;
                }
            }
            int tmp = array[j];
            while (j > i) {
                array[j] = array[j - 1];
                j--;
            }
            array[i] = tmp;
        }
    }
}
```



###### 14.链表中倒数第k个结点

​	输入一个链表，输出该链表中倒数第k个结点。

```java
public class Solution {
    public ListNode FindKthToTail(ListNode head, int k) {
        ListNode p = head;
        ListNode q = head;
        while(q != null) {
            q = q.next;
            if (--k == 0) {
                break;
            }
        }
        if (q == null && k == 0) {
            return head;
        }
        if (q == null) {
            return null;
        }
        while (q != null) {
            p = p.next;
            q = q.next;
        }
        return p;
    }
}
```



###### 15.反转链表

​	输入一个链表，反转链表后，输出新链表的表头。

```java
public class Solution {
    public ListNode ReverseList(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }
        ListNode pre = null;
        ListNode cur = head;
        ListNode next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
}
```

