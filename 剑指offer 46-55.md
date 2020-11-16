---
title: 剑指offer 46-55
date: 2020-10-31 13:26:24
categories: 算法
tags: 剑指offer
---

###### 46.孩子们的游戏(圆圈中最后剩下的数)

​	每年六一儿童节,牛客都会准备一些小礼物去看望孤儿院的小朋友,今年亦是如此。HF作为牛客的资深元老,自然也准备了一些小游戏。其中,有个游戏是这样的:首先,让小朋友们围成一个大圈。然后,他随机指定一个数m,让编号为0的小朋友开始报数。每次喊到m-1的那个小朋友要出列唱首歌,然后可以在礼品箱中任意的挑选礼物,并且不再回到圈中,从他的下一个小朋友开始,继续0...m-1报数....这样下去....直到剩下最后一个小朋友,可以不用表演,并且拿到牛客名贵的“名侦探柯南”典藏版(名额有限哦!!^_^)。请你试着想下,哪个小朋友会得到这份礼品呢？(注：小朋友的编号是从0到n-1)

```java
public class Solution {
    public int LastRemaining_Solution(int n, int m) {
        if(m==0||n==0){
            return -1;
        }
        List<Integer> res=new ArrayList<>();
        for(int i=0;i<n;i++){
            res.add(i);
        }
        int index=-1;
        while(res.size()>1){
            index=(index+m)%res.size();
            res.remove(index);
            index--;
        }
        return res.get(0);
    }
}

```



###### 47.求1+2+3+...+n

​	求1+2+3+...+n，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句（A?B:C）。

```java
public class Solution {
    public int Sum_Solution(int n) {
        int sum=n;
        boolean flag=n>0&&(sum+=Sum_Solution(n-1))>0;
        return sum;
    }
}
```



###### 48.不用加减乘除做加法

​	写一个函数，求两个整数之和，要求在函数体内不得使用+、-、*、/四则运算符号。

```java
public class Solution {
    public int Add(int num1,int num2) {
        int tmp=0;
        while(num1!=0){
            tmp=(num1^num2);
            num1=((num1&num2)<<1);
            num2=tmp;
        }
        return num2;
    }
}

```



###### 49.把字符串转换成整数

​	将一个字符串转换成一个整数，要求不能使用字符串转换整数的库函数。 数值为0或者字符串不是一个合法的数值则返回0

```java
public class Solution {
    public int StrToInt(String str) {
        if(str==null||str.length()==0){
            return 0;
        }
        boolean flag=true;
        int index=0;
        if(str.charAt(index)=='+'){
            index++;
        }else if(str.charAt(index)=='-'){
            index++;
            flag=false;
        }
        int sum=0;
        for(int i=index;i<str.length();i++){
            if(str.charAt(i)<'0'||str.charAt(i)>'9'){
                return 0;
            }
            int p = str.charAt(i)-'0';
            if(flag&&((sum==Integer.MAX_VALUE/10&&p>7)||sum>Integer.MAX_VALUE/10)){
                return Integer.MAX_VALUE;
            }
            if(!flag&&((-sum==Integer.MIN_VALUE/10&&p>8)||-sum<Integer.MIN_VALUE/10)){
                return Integer.MIN_VALUE;
            }
            sum=sum*10+p;
        }
        return flag?sum:-sum;
    }
}
```



###### 50.数组中重复的数字

​	在一个长度为n的数组里的所有数字都在0到n-1的范围内。 数组中某些数字是重复的，但不知道有几个数字是重复的。也不知道每个数字重复几次。请找出数组中第一个重复的数字。 例如，如果输入长度为7的数组{2,3,1,0,2,5,3}，那么对应的输出是第一个重复的数字2。

```java
public class Solution {
    // Parameters:
    //    numbers:     an array of integers
    //    length:      the length of array numbers
    //    duplication: (Output) the duplicated number in the array number,length of duplication array is 1,so using duplication[0] = ? in implementation;
    //                  Here duplication like pointor in C/C++, duplication[0] equal *duplication in C/C++
    //    这里要特别注意~返回任意重复的一个，赋值duplication[0]
    // Return value:       true if the input is valid, and there are some duplications in the array number
    //                     otherwise false
    public boolean duplicate(int numbers[],int length,int [] duplication) {
        if(length==0){
            return false;
        }
        for(int i=0;i<length;i++){
            while(numbers[i]!=i){
                if(numbers[i]==numbers[numbers[i]]){
                    duplication[0]=numbers[i];
                    return true;
                }else {
                    int tmp=numbers[i];
                    numbers[i]=numbers[tmp];
                    numbers[tmp]=tmp;
                }
            }
             
        }
        return false;
    }
}
```



###### 51.构建乘积数组

​	给定一个数组A[0,1,...,n-1],请构建一个数组B[0,1,...,n-1],其中B中的元素B[i]=A[0]*A[1]*...*A[i-1]*A[i+1]*...*A[n-1]。不能使用除法。（注意：规定B[0] = A[1] * A[2] * ... * A[n-1]，B[n-1] = A[0] * A[1] * ... * A[n-2];）

对于A长度为1的情况，B无意义，故而无法构建，因此该情况不会存在。

```java
public class Solution {
    public int[] multiply(int[] A) {
        int len=A.length;
        if(len==0){
            return A;
        }
        int[] B=new int[len];
        int tmp=1;
        for(int i=0;i<len;i++){
            B[i]=tmp;
            tmp*=A[i];
        }
        tmp=1;
        for(int i=len-1;i>=0;i--){
            B[i]*=tmp;
            tmp*=A[i];
        }
        return B;
    }
}
```



###### 52.正则表达式匹配

​	请实现一个函数用来匹配包括'.'和'*'的正则表达式。模式中的字符'.'表示任意一个字符，而'*'表示它前面的字符可以出现任意次（包含0次）。 在本题中，匹配是指字符串的所有字符匹配整个模式。例如，字符串"aaa"与模式"a.a"和"ab*ac*a"匹配，但是与"aa.a"和"ab*a"均不匹配

```java
public class Solution {
    Integer[][] memo;
    public boolean match(char[] str, char[] p)
    {
        int m=str.length;
        int n=p.length;
        memo = new Integer[m+1][n+1];
        return help(str,p,0,0);
    }
     
    private boolean help(char[] c,char[] p,int i,int j){
        if(j==p.length){
            return i==c.length;
        }
        if(memo[i][j]!=null){
            return memo[i][j]!=-1;
        }
        boolean firstMatch = i<c.length&&j<p.length&&(c[i]==p[j]||p[j]=='.');
        boolean ans;
        if(j+1<p.length&&p[j+1]=='*'){
            ans=help(c,p,i,j+2)||(firstMatch&&help(c,p,i+1,j));
        }else{
            ans=help(c,p,i+1,j+1)&&firstMatch;
        }
        memo[i][j]=ans?1:-1;
        return ans;
    }
}
```



###### 53.表示数值的字符串

​	请实现一个函数用来判断字符串是否表示数值（包括整数和小数）。

```java
public class Solution {
    int index=0;
    public boolean isNumeric(char[] str) {
        boolean flag = isPoNum(str);
        if(index<str.length&&str[index]=='.'){
            index++;
            flag = isNum(str)||flag;
        }
        if(index<str.length&&(str[index]=='e'||str[index]=='E')){
            index++;
            flag = isPoNum(str)&&flag;
        }
        return flag&&index==str.length;
    }
     
    private boolean isPoNum(char[] str){
        if(index<str.length&&(str[index]=='+'||str[index]=='-'))
            index++;
        return isNum(str);
    }
     
    private boolean isNum(char[] str){
        int start=index;
        while(index<str.length&&(str[index]>='0'&&str[index]<='9')){
            index++;
        }
        return index>start;
    }
}
```



###### 54.字符流中第一个不重复的字符

​	请实现一个函数用来找出字符流中第一个只出现一次的字符。例如，当从字符流中只读出前两个字符"go"时，第一个只出现一次的字符是"g"。当从该字符流中读出前六个字符“google"时，第一个只出现一次的字符是"l"。

```java
public class Solution {
    //Insert one char from stringstream
    Queue<Character> queue=new LinkedList<>();
    int[] map = new int[256];
    public void Insert(char ch)
    {
        map[ch]++;
        if(map[ch]==1){
            queue.add(ch);
        }
    }
  //return the first appearence once char in current stringstream
    public char FirstAppearingOnce()
    {
        char c = '#';
        while(!queue.isEmpty()){
            c=queue.peek();
            if(map[c]==1){
                return c;
            }else{
                queue.poll();
            }
        }
        return '#';
    }
}
```



###### 55.链表中环的入口结点

给一个链表，若其中包含环，请找出该链表的环的入口结点，否则，输出null。

```java
public class Solution {
 
    public ListNode EntryNodeOfLoop(ListNode head)
    {
        ListNode p=head;
        ListNode q=head;
        while(q!=null&&q.next!=null){
            p=p.next;
            q=q.next.next;
            if(p==q){
                break;
            }
        }
         
        if(q==null||q.next==null){
            return null;
        }
        p=head;
        while(p!=q){
            p=p.next;
            q=q.next;
        }
        return p;
    }
}

```

