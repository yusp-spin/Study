---
title: 剑指offer 56-67
date: 2020-11-01 9:26:03
categories: 算法
tags: 剑指offer
---

###### 56.删除链表中重复的结点

​	在一个排序的链表中，存在重复的结点，请删除该链表中重复的结点，重复的结点不保留，返回链表头指针。 例如，链表1->2->3->3->4->4->5 处理后为 1->2->5

```java
public class Solution {
    public ListNode deleteDuplication(ListNode head)
    {
        ListNode res=new ListNode(0);
        res.next=head;
        ListNode pre=res;
        ListNode cur=head;
        while(cur!=null){
            if(cur.next!=null&&cur.val==cur.next.val){
                while(cur.next!=null&&cur.val==cur.next.val){
                    cur=cur.next;
                }
                cur=cur.next;
                pre.next=cur;
            }else{
                pre=pre.next;
                cur=cur.next;
            }
        }
        return res.next;
    }
}
```



###### 57.二叉树的下一个结点

​	给定一个二叉树和其中的一个结点，请找出中序遍历顺序的下一个结点并且返回。注意，树中的结点不仅包含左右子结点，同时包含指向父结点的指针。

```java
public class Solution {
    public TreeLinkNode GetNext(TreeLinkNode root)
    {
        if(root==null){
            return root;
        }
        if(root.right!=null){
            root=root.right;
            while(root.left!=null){
                root=root.left;
            }
            return root;
        }
        if(root.next!=null&&root.next.left==root){
            return root.next;
        }
        while(root.next!=null&&root.next.right==root){
            root=root.next;
        }
        return root.next;
    }
}
```



###### 58.对称的二叉树

```java
public class Solution {
    boolean isSymmetrical(TreeNode root)
    {
        if(root==null){
            return true;
        }
        return help(root.left,root.right);
    }
     
    private boolean help(TreeNode p,TreeNode q){
        if(p==null&&q==null){
            return true;
        }
        if(p==null||q==null){
            return false;
        }
        if(p.val!=q.val){
            return false;
        }
        return help(p.left,q.right)&&help(p.right,q.left);
    }
}
```



###### 59.按之字形顺序打印二叉树

​	请实现一个函数按照之字形打印二叉树，即第一行按照从左到右的顺序打印，第二层按照从右至左的顺序打印，第三行按照从左到右的顺序打印，其他行以此类推。

```java
public class Solution {
    public ArrayList<ArrayList<Integer> > Print(TreeNode root) {
         ArrayList<ArrayList<Integer> > res=new ArrayList<>();
        Queue<TreeNode> queue=new LinkedList<>();
        if(root==null){
            return res;
        }
        int index=0;
        queue.add(root);
        while(!queue.isEmpty()){
            int size=queue.size();
            ArrayList<Integer> list=new ArrayList<>();
            for(int i=0;i<size;i++){
                TreeNode node=queue.poll();
                if(index%2==0){
                    list.add(node.val);
                }else{
                    list.add(0,node.val);
                }
                if(node.left!=null){
                    queue.add(node.left);
                }
                if(node.right!=null){
                    queue.add(node.right);
                }
            }
            index++;
            res.add(new ArrayList<>(list));
        }
        return res;
    }
 
}
```



###### 60.把二叉树打印成多行

​	从上到下按层打印二叉树，同一层结点从左至右输出。每一层输出一行。

```java
public class Solution {
    ArrayList<ArrayList<Integer> > Print(TreeNode root) {
        ArrayList<ArrayList<Integer> > res=new ArrayList<>();
        Queue<TreeNode> queue=new LinkedList<>();
        if(root==null){
            return res;
        }
        queue.add(root);
        while(!queue.isEmpty()){
            int size=queue.size();
            ArrayList<Integer> list=new ArrayList<>();
            for(int i=0;i<size;i++){
                TreeNode node=queue.poll();
                list.add(node.val);
                if(node.left!=null){
                    queue.add(node.left);
                }
                if(node.right!=null){
                    queue.add(node.right);
                }
            }
            res.add(new ArrayList<>(list));
        }
        return res;
    }
     
}
```



###### 61.序列化二叉树

请实现两个函数，分别用来序列化和反序列化二叉树

二叉树的序列化是指：把一棵二叉树按照某种遍历方式的结果以某种格式保存为字符串，从而使得内存中建立起来的二叉树可以持久保存。序列化可以基于先序、中序、后序、层序的二叉树遍历方式来进行修改，序列化的结果是一个字符串，序列化时通过 某种符号表示空节点（#），以 ！ 表示一个结点值的结束（value!）。

二叉树的反序列化是指：根据某种遍历顺序得到的序列化字符串结果str，重构二叉树。

例如，我们可以把一个只有根节点为1的二叉树序列化为"1,"，然后通过自己的函数来解析回这个二叉树

```java
public class Solution {
    String Serialize(TreeNode root) {
        String res = "";
        if(root==null){
            return "#!";
        }
        res+=root.val+"!";
        res+=Serialize(root.left);
        res+=Serialize(root.right);
        return res;
  }
    TreeNode Deserialize(String str) {
       if(str == null || str.length()==0){
           return null;
       }
        Queue<String> queue = new LinkedList<>();
        String[] s= str.split("!");
        for(int i=0;i<s.length;i++){
            queue.add(s[i]);
        }
        return Deserialize(queue);
  }
     
    private TreeNode Deserialize(Queue<String> queue){
        if(queue.isEmpty()){
            return null;
        }
        String s=queue.poll();
        if(s.equals("#")){
            return null;
        }
        TreeNode root = new TreeNode(Integer.valueOf(s));
        root.left=Deserialize(queue);
        root.right=Deserialize(queue);
        return root;
    }
}
```



###### 62.二叉搜索树的第k个结点

​	给定一棵二叉搜索树，请找出其中的第k小的结点。

```java
public class Solution {
    TreeNode KthNode(TreeNode root, int k)
    {
        //中序遍历
        if(root==null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<>();
        while(!stack.isEmpty()||root!=null) {
            while(root!=null){
                stack.push(root);
                root=root.left;
            }
            root=stack.pop();
            if(--k==0){
                return root;
            }
            root=root.right;
        }
        return null;
    }
}
```



###### 63.数据流中的中位数

​	如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。我们使用Insert()方法读取数据流，使用GetMedian()方法获取当前读取数据的中位数。

```java
public class Solution {
 
    PriorityQueue<Integer> minqueue = new PriorityQueue<>();
    PriorityQueue<Integer> maxqueue = new PriorityQueue<>(new Comparator<Integer>(){
        public int compare(Integer a,Integer b) {
            return b-a;
        }
    });
    int index=0;
    public void Insert(Integer num) {
        if(index%2==0){
            minqueue.add(num);
            maxqueue.add(minqueue.poll());
        }else{
            maxqueue.add(num);
            minqueue.add(maxqueue.poll());
        }
        index++;
    }
 
    public Double GetMedian() {
        if(index%2==1){
            return maxqueue.peek()/1.0;
        }else{
            return (maxqueue.peek()+minqueue.peek())/2.0;
        }
    }
}

```



###### 64.滑动窗口的最大值

​	给定一个数组和滑动窗口的大小，找出所有滑动窗口里数值的最大值。例如，如果输入数组{2,3,4,2,6,2,5,1}及滑动窗口的大小3，那么一共存在6个滑动窗口，他们的最大值分别为{4,4,6,6,6,5}； 针对数组{2,3,4,2,6,2,5,1}的滑动窗口有以下6个： {[2,3,4],2,6,2,5,1}， {2,[3,4,2],6,2,5,1}， {2,3,[4,2,6],2,5,1}， {2,3,4,[2,6,2],5,1}， {2,3,4,2,[6,2,5],1}， {2,3,4,2,6,[2,5,1]}。

窗口大于数组长度的时候，返回空

```java
public class Solution {
    public ArrayList<Integer> maxInWindows(int [] num, int size)
    {
        ArrayList<Integer> res = new ArrayList<>();
        int len=num.length;
        if(num.length<size||size<1){
            return res;
        }
        LinkedList<Integer> queue = new LinkedList<>();
        for(int i=0;i<len;i++){
            while(!queue.isEmpty()&&num[queue.peekLast()]<=num[i]){
                queue.pollLast();
            }
            queue.addLast(i);
            if(queue.peekFirst()==i-size){
                queue.pollFirst();
            }
            if(i>=size-1){
                res.add(num[queue.peekFirst()]);
            }
        }
        return res;
    }
}
```



###### 65.单词搜索

```java
public class Solution {
    char[][] board;
    boolean[][] used;
    int rows;
    int cols;
    char[] str;
    int[][] dep={{1,0},{0,1},{-1,0},{0,-1}};
    public boolean hasPath(char[] matrix, int rows, int cols, char[] str)
    {
        this.rows=rows;
        this.cols=cols;
        this.board = new char[rows][cols];
        this.used = new boolean[rows][cols];
        this.str=str;
        int p=0;
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                board[i][j]=matrix[p++];
            }
        }
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(dfs(i,j,0)){
                    return true;
                }
            }
        }
        return false;
    }
     
    private boolean dfs(int i,int j,int index){
       if(index==str.length-1){
           return board[i][j]==str[index];
       }
        //if(used[i][j]){
        //    return false;
      //  }
       if(board[i][j]==str[index]){
           used[i][j]=true;
           for(int a=0;a<4;a++){
               int x=i+dep[a][0];
               int y=j+dep[a][1];
               if(x>=0&&x<rows&&y>=0&&y<cols&&!used[x][y]){
                   if(dfs(x,y,index+1)){
                       return true;
                   }
               }
           }
           used[i][j]=false;
       }
        return false;
    }
 
 
```



###### 66.机器人的运动范围

​	地上有一个m行和n列的方格。一个机器人从坐标0,0的格子开始移动，每一次只能向左，右，上，下四个方向移动一格，但是不能进入行坐标和列坐标的数位之和大于k的格子。 例如，当k为18时，机器人能够进入方格（35,37），因为3+5+3+7 = 18。但是，它不能进入方格（35,38），因为3+5+3+8 = 19。请问该机器人能够达到多少个格子？

```java
public class Solution {
    int threshold;
    int rows;
    int cols;
    boolean[][] used;
    public int movingCount(int threshold, int rows, int cols)
    {
        this.threshold = threshold;
        this.rows = rows;
        this.cols = cols;
        used = new boolean[rows][cols];
        return help(0,0);
    }
     
    private int help(int i,int j) {
        if(i<0||i>=rows||j<0||j>=cols||((isSum(i)+isSum(j))>threshold)||used[i][j]) {
            return 0;
        }
        used[i][j]=true;
        return help(i-1,j)+help(i,j-1)+help(i+1,j)+help(i,j+1)+1;
    }
     
    private int isSum(int i) {
        int sum = 0;
        while(i!=0){
            sum+=(i%10);
            i/=10;
        }
        return sum;
    }
}
```



###### 67.剪绳子

​	给你一根长度为n的绳子，请把绳子剪成整数长的m段（m、n都是整数，n>1并且m>1，m<=n），每段绳子的长度记为k[1],...,k[m]。请问k[1]x...xk[m]可能的最大乘积是多少？例如，当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此时得到的最大乘积是18。

```java
public class Solution {
    public int cutRope(int target) {
        if (target <= 3) {
            return target - 1;
        }
        int [] dp = new int[target + 1];
      //  dp[0] = 0;
        //dp[1] = 1;
        dp[2] = 2;
        dp[3] = 3;
        for(int i = 4; i <= target; i++) {
     //       for(int j = 1; j <= i / 2; j++) {
                dp[i] = Math.max(dp[i-2]*2, dp[i-3]*3);
       //     }
        }
        return dp[target];
    }
}
```

