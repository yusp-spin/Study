---
title: 剑指offer 36-45
date: 2020-10-30 18:26:24
categories: 算法
tags: 剑指offer
---

###### 36.两个链表的第一个公共节点

​	输入两个链表，找出它们的第一个公共结点。（注意因为传入数据是链表，所以错误测试数据的提示是用其他方式显示的，保证传入数据是正确的）

```java
public class Solution {
    public ListNode FindFirstCommonNode(ListNode head1, ListNode head2) {
        ListNode p=head1;
        ListNode q=head2;
        while(p!=q){
            p=p==null?head2:p.next;
            q=q==null?head1:q.next;
        }
        return p;
    }
}
```



###### 37.数字在排序数组中出现的次数

​	统计一个数字在升序数组中出现的次数。

```java
public class Solution {
    public int GetNumberOfK(int [] array , int k) {
       int len=array.length;
        if(len==0){
            return 0;
        }
        int l=help(array,k);
        int r=help(array,k+1);
        return r>l?r-l:0;
    }
     
    private int help(int[] arr,int k){
        int l=0;
        int r=arr.length-1;
        while(l<=r){
            int mid=(l+r)/2;
            if(arr[mid]<k){
                l=mid+1;
            }else{
                r=mid-1;
            }
        }
        return l;
    }
}
```



###### 38.二叉树的深度

​	输入一棵二叉树，求该树的深度。从根结点到叶结点依次经过的结点（含根、叶结点）形成树的一条路径，最长路径的长度为树的深度。

```java
public class Solution {
    public int TreeDepth(TreeNode root) {
        if(root==null){
            return 0;
        }
        return Math.max(TreeDepth(root.left),TreeDepth(root.right))+1;
    }
}

```



###### 39.平衡二叉树

输入一棵二叉树，判断该二叉树是否是平衡二叉树。

```java
public class Solution {
    public boolean IsBalanced_Solution(TreeNode root) {
        if(root==null){
            return true;
        }
        return help(root)!=-1;
    }
    private int help(TreeNode root){
        if(root==null){
            return 0;
        }
        int left=help(root.left);
        if(left==-1){
            return -1;
        }
        int right=help(root.right);
        if(right==-1){
            return -1;
        }
        return Math.abs(left-right)<=1?Math.max(left,right)+1:-1;
    }
}

```



###### 40.数组中只出现一次的数字

​	一个整型数组里除了两个数字之外，其他的数字都出现了两次。请写程序找出这两个只出现一次的数字。

```java
public class Solution {
    public void FindNumsAppearOnce(int [] array,int num1[] , int num2[]) {
        int val=0;
        for(int num:array){
            val^=num;
        }
        int index=1;
        while((index&val)==0){
            index<<=1;
        }
        num1[0]=0;
        num2[0]=0;
        for(int num:array){
            if((num&index)==0){
                num1[0]^=num;
            }else{
                num2[0]^=num;
            }
        }
    }
}
```



###### 41.和为S的连续正数序列

​	小明很喜欢数学,有一天他在做数学作业时,要求计算出9~16的和,他马上就写出了正确答案是100。但是他并不满足于此,他在想究竟有多少种连续的正数序列的和为100(至少包括两个数)。没多久,他就得到另一组连续正数和为100的序列:18,19,20,21,22。现在把问题交给你,你能不能也很快的找出所有和为S的连续正数序列? Good Luck!

```java
public class Solution {
    public ArrayList<ArrayList<Integer> > FindContinuousSequence(int sum) {
       ArrayList<ArrayList<Integer> >res=new ArrayList<>();
        int l=1;
        int r=2;
        while(r<sum){
            int tmp=(l+r)*(r-l+1)/2;
            if(tmp==sum){
                ArrayList<Integer> list=new ArrayList<Integer>();
                for(int i=l;i<=r;i++){
                    list.add(i);
                }
                res.add(new ArrayList<>(list));
                l++;
            }else if(tmp<sum){
                r++;
            }else{
                l++;
            }
        }
        return res;
    }
}
```



###### 42.和为S的两个数字

​	输入一个递增排序的数组和一个数字S，在数组中查找两个数，使得他们的和正好是S，如果有多对数字的和等于S，输出两个数的乘积最小的。

```java
public class Solution {
    public ArrayList<Integer> FindNumbersWithSum(int [] array,int sum) {
        ArrayList<Integer> res=new ArrayList<>();
        int len=array.length;
        if(len==0){
            return res;
        }
        int l=0;
        int r=len-1;
        while(l<r){
            int tmp=array[l]+array[r];
            if(tmp==sum){
                res.add(array[l]);
                res.add(array[r]);
                return res;
            }else if(tmp>sum){
                r--;
            }else{
                l++;
            }
        }
        return res;
    }
}
```



###### 43.左旋转字符串

​	汇编语言中有一种移位指令叫做循环左移（ROL），现在有个简单的任务，就是用字符串模拟这个指令的运算结果。对于一个给定的字符序列S，请你把其循环左移K位后的序列输出。例如，字符序列S=”abcXYZdef”,要求输出循环左移3位后的结果，即“XYZdefabc”。是不是很简单？OK，搞定它！

```java
public class Solution {
    public String LeftRotateString(String str,int n) {
        if(n>str.length()){
            return "";
        }
        return str.substring(n,str.length())+str.substring(0,n);
    }
}

```



###### 44.翻转单词顺序列

​	牛客最近来了一个新员工Fish，每天早晨总是会拿着一本英文杂志，写些句子在本子上。同事Cat对Fish写的内容颇感兴趣，有一天他向Fish借来翻看，但却读不懂它的意思。例如，“student. a am I”。后来才意识到，这家伙原来把句子单词的顺序翻转了，正确的句子应该是“I am a student.”。Cat对一一的翻转这些单词顺序可不在行，你能帮助他么？

```java
public class Solution {
    public String ReverseSentence(String str) {
        if(str.trim().length()==0){
            return str;
        }
        String[] c=str.split("\\s+");
        for(int i=0;i<c.length/2;i++){
            String tmp=c[i];
            c[i]=c[c.length-1-i];
            c[c.length-1-i]=tmp;
        }
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<c.length;i++){
            sb.append(c[i]);
            if(i!=c.length-1){
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
```



###### 45.扑克牌顺子

​	LL今天心情特别好,因为他去买了一副扑克牌,发现里面居然有2个大王,2个小王(一副牌原本是54张^_^)...他随机从中抽出了5张牌,想测测自己的手气,看看能不能抽到顺子,如果抽到的话,他决定去买体育彩票,嘿嘿！！“红心A,黑桃3,小王,大王,方片5”,“Oh My God!”不是顺子.....LL不高兴了,他想了想,决定大\小 王可以看成任何数字,并且A看作1,J为11,Q为12,K为13。上面的5张牌就可以变成“1,2,3,4,5”(大小王分别看作2和4),“So Lucky!”。LL决定去买体育彩票啦。 现在,要求你使用这幅牌模拟上面的过程,然后告诉我们LL的运气如何， 如果牌能组成顺子就输出true，否则就输出false。为了方便起见,你可以认为大小王是0。

```java
public class Solution {
    public boolean isContinuous(int [] numbers) {
        int len=numbers.length;
        if(len<5){
            return false;
        }
        int count=0;
        int p=0;
        Arrays.sort(numbers);
        for(int i=0;i<len;i++){
            if(numbers[i]==0){
                count++;
            }else{
                if(i<len-1&&numbers[i]==numbers[i+1]){
                    return false;
                }
                if(i<len-1){
                    p+=(numbers[i+1]-numbers[i]-1);
                }
            }
             
        }
        return count>=p;
    }
}
```

