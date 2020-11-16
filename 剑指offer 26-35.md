---
title: 剑指offer 26-35
date: 2020-10-27 22:59:02
categories: 算法
tags: 剑指offer
---

###### 26. 二叉搜索树与双向链表

​	输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。要求不能创建任何新的结点，只能调整树中结点指针的指向。

```java
public class Solution {
    TreeNode head;
    TreeNode cur;
    public TreeNode Convert(TreeNode root) {
        if(root==null){
            return root;
        }
        inorder(root);
        return head;
    }
    private void inorder(TreeNode root){
        Stack<TreeNode> stack=new Stack<>();
        while(root!=null||!stack.isEmpty()){
            while(root!=null){
                stack.push(root);
                root=root.left;
            }
            root=stack.pop();
            if(head==null){
                head=root;
                cur=root;
            }else{
                cur.right=root;
                root.left=cur;
                cur=cur.right;
            }
            root=root.right;
        }
    }
}
```



###### 27.字符串的排列

​	输入一个字符串,按字典序打印出该字符串中字符的所有排列。例如输入字符串abc,则按字典序打印出由字符a,b,c所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。

```java
public class Solution {
    TreeSet<String> res=new TreeSet<>();
    ArrayList<Character> list=new ArrayList<>();
    boolean[] used;
    public ArrayList<String> Permutation(String str) {
       if(str==null||str.length()==0){
           return new ArrayList<>();
       }
        used=new boolean[str.length()];
       help(str.toCharArray(),0);
       return new ArrayList<>(res);
    }
    private void help(char[] c,int index){
        if(index==c.length){
            StringBuffer sb=new StringBuffer();
            for(char cs:list){
                sb.append(cs);
            }
            res.add(sb.toString());
            return;
        }
        for(int i=0;i<c.length;i++){
            if(used[i]){
                continue;
            }
            used[i]=true;
            list.add(c[i]);
            help(c,index+1);
            list.remove(list.size()-1);
            used[i]=false;
        }
    }
}
```

​	

28.数组中出现次数超过一半的数字

​	数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。例如输入一个长度为9的数组{1,2,3,2,2,2,5,4,2}。由于数字2在数组中出现了5次，超过数组长度的一半，因此输出2。如果不存在则输出0。

```java
public class Solution {
    public int MoreThanHalfNum_Solution(int [] array) {
        int count=0;
        int candidate=0;
        int len=array.length;
        if(len==0){
            return 0;
        }
        for(int i=0;i<len;i++){
            if(count==0){
                candidate=array[i];
                count++;
            }else{
                if(candidate==array[i]){
                    count++;
                }else{
                    count--;
                }
            }
        }
        if(count==0){
            return 0;
        }
        count=0;
        for(int n:array){
            if(candidate==n){
                count++;
            }
        }
        return count>len/2?candidate:0;
    }
}
```



###### 29.最小的k个数

​	输入n个整数，找出其中最小的K个数。例如输入4,5,1,6,2,7,3,8这8个数字，则最小的4个数字是1,2,3,4。

```java
public class Solution {
    public ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
        if(input.length<k||k<=0){
            return new ArrayList<>();
        }
        PriorityQueue<Integer> queue=new PriorityQueue<>(new Comparator<Integer>(){
            public int compare(Integer a,Integer b){
                return b-a;
            }
        });
        for(int i=0;i<input.length;i++){
            if(i<k){
                queue.add(input[i]);
            }else{
                if(queue.peek()>input[i]){
                    queue.poll();
                    queue.add(input[i]);
                }
            }
        }
        return new ArrayList<>(queue);
    }
}
```



###### 30.连续子数组的最大和

​	返回最大连续子序列的和

```java
public class Solution {
    public int FindGreatestSumOfSubArray(int[] array) {
        int max=array[0];
        int res=array[0];
        for(int i=1;i<array.length;i++){
            max=Math.max(max+array[i],array[i]);
            res=Math.max(res,max);
        }
        return res;
    }
}
```



###### 31.整数中1出现的次数

​	求出1~13的整数中1出现的次数,并算出100~1300的整数中1出现的次数？为此他特别数了一下1~13中包含1的数字有1、10、11、12、13因此共出现6次,但是对于后面问题他就没辙了。ACMer希望你们帮帮他,并把问题更加普遍化,可以很快的求出任意非负整数区间中1出现的次数（从1 到 n 中1出现的次数）。

```java
public class Solution {
    public int NumberOf1Between1AndN_Solution(int n) {
        int count=0;
        for(int m=1;m<=n;m*=10){
            int a=n/m;
            int b=n%m;
            count+=(a+8)/10*m+(a%10==1?b+1:0);
        }
        return count;
    }
}
```



###### 32.把数组排成最小的数

​	输入一个正整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。例如输入数组{3，32，321}，则打印出这三个数字能排成的最小数字为321323。

```java
public class Solution {
    public String PrintMinNumber(int [] numbers) {
        int len=numbers.length;
        if(len==0){
            return "";
        }
        Integer[] arr=new Integer[len];
        int index=0;
        for(int n:numbers){
            arr[index++]=n;
        }
        Arrays.sort(arr,new Comparator<Integer>(){
            public int compare(Integer a,Integer b){
                return (a+""+b).compareTo(b+""+a);
            }
        });
        StringBuffer sb=new StringBuffer();
        for(Integer a:arr){
            sb.append(a);
        }
        return sb.toString();
    }
}
```



###### 33.丑数

​	把只包含质因子2、3和5的数称作丑数（Ugly Number）。例如6、8都是丑数，但14不是，因为它包含质因子7。 习惯上我们把1当做是第一个丑数。求按从小到大的顺序的第N个丑数。

```java
public class Solution {
    public int GetUglyNumber_Solution(int index) {
        if(index<1){
            return 0;
        }
        int[] dp=new int[index];
        dp[0]=1;
        int a=0;
        int b=0;
        int c=0;
        for(int i=1;i<index;i++){
            dp[i]=Math.min(dp[a]*2,Math.min(dp[b]*3,dp[c]*5));
            if(dp[i]==dp[a]*2){
                a++;
            }
            if(dp[i]==dp[b]*3){
                b++;
            }
            if(dp[i]==dp[c]*5){
                c++;
            }
        }
        return  dp[index-1];
    }
}
```



###### 34.第一次只出现一次的字符

​	在一个字符串(0<=字符串长度<=10000，全部由字母组成)中找到第一个只出现一次的字符,并返回它的位置, 如果没有则返回 -1（需要区分大小写）.（从0开始计数）

```java
public class Solution {
     
    public int FirstNotRepeatingChar(String str) {
        int[] arr=new int[128];
        char[] cs=str.toCharArray();
        for(char p:cs){
            arr[p]++;
        }
        for(int i=0;i<cs.length;i++){
            if(arr[cs[i]]==1){
                return i;
            }
        }
        return -1;
    }
}
```



###### 35.数组中的逆序对

​	在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组,求出这个数组中的逆序对的总数P。并将P对1000000007取模的结果输出。 即输出P%1000000007

```java
public class Solution {
    public int InversePairs(int [] array) {
        if(array == null || array.length == 0) {
            return 0;
        }
        return mergesort(array, 0, array.length - 1);
    }
 
private int mergesort(int[] arr, int l, int r) {
    if(l >= r) {
        return 0;
    }
    int mid = l + (r - l) / 2;
    return (mergesort(arr, l, mid) + mergesort(arr, mid + 1, r) + merge(arr, l, mid, r)) % 1000000007;
}
 
    private int merge(int[] arr, int l, int mid ,int r) {
        int[] help = new int[r - l + 1];
        int p = l;
        int q = mid + 1;
        int index = 0;
        int sum = 0;
        while(p <= mid && q <= r) {
            if(arr[p] <= arr [q]) {
                help[index++] = arr[p++];
            }else if( arr[p] > arr[q]){
                sum = (sum + mid - p + 1) % 1000000007;
                help[index++] = arr[q++];
            }
        }
        while(p <= mid) {
            help[index++] = arr[p++];
        }
        while( q <= r) {
            help[index++] = arr[q++];
        }
        for(int i = l; i <= r; i++) {
            arr[i] = help[i - l];
        }
        return sum % 1000000007;
    }
}
```

