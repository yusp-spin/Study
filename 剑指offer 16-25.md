---
title: 剑指offer 16-25
date: 2020-10-20 21:54:02
categories: 算法
tags: 剑指offer
---

###### 16.  合并两个有序链表

​	输入两个单调递增的链表，输出两个链表合成后的链表，当然我们需要合成后的链表满足单调不减规则。

```java
public class Solution {
    public ListNode Merge(ListNode list1, ListNode list2) {
        ListNode head = new ListNode(0);
        ListNode p = head;
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                p.next = list1;
                list1 = list1.next;
            } else {
                p.next = list2;
                list2 = list2.next;
            }
            p = p.next;
        }
        if (list1 != null) {
            p.next = list1;
        }
        if (list2 != null) {
            p.next = list2;
        }
        return head.next;
    }
}

```



###### 17.树的子结构

​	输入两棵二叉树A，B，判断B是不是A的子结构。（ps：我们约定空树不是任意一个树的子结构）

```java
public class Solution {
    public boolean HasSubtree(TreeNode root1, TreeNode root2) {
        if (root1 == null || root2 == null) {
            return false;
        }
        return help(root1, root2) || HasSubtree(root1.left, root2) || HasSubtree(root1.right, root2);
    }
    private boolean help(TreeNode root1, TreeNode root2) {
        if (root2 == null) {
            return true;
        }
        if (root1 == null) {
            return false;
        }
        if (root1.val != root2.val) {
            return false;
        }
        return help(root1.left, root2.left) && help(root1.right, root2.right);
    }
}
```



###### 18.二叉树的镜像

​	操作给定的二叉树，将其变换为源二叉树的镜像。

```java
public class Solution {
    public void Mirror(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        Mirror(root.left);
        Mirror(root.right);
    }
}
```



###### 19.顺时针打印矩阵

​	输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字，例如，如果输入如下4 X 4矩阵： 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 则依次打印出数字1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10.

```java
import java.util.ArrayList;
public class Solution {
    public ArrayList<Integer> printMatrix(int [][] matrix) {
        ArrayList<Integer> res=new ArrayList<>();
       int m=matrix.length;
        if(m==0){
            return res;
        }
        int n=matrix[0].length;
        int left=0;
        int right=n-1;
        int up=0;
        int down=m-1;
        while(true){
            for(int i=left;i<=right;i++){
                res.add(matrix[up][i]);
            }
            if(++up>down){
                break;
            }
            for(int i=up;i<=down;i++){
                res.add(matrix[i][right]);
            }
            if(--right<left){
                break;
            }
            for(int i=right;i>=left;i--){
                res.add(matrix[down][i]);
            }
            if(--down<up){
                break;
            }
            for(int i=down;i>=up;i--){
                res.add(matrix[i][left]);
            }
            if(++left>right){
                break;
            }
        }
        return res;
    }
}
```



###### 20.包含min函数的栈

​	定义栈的数据结构，请在该类型中实现一个能够得到栈中所含最小元素的min函数（时间复杂度应为O（1））。

```java
public class Solution {
 
    Stack<Integer> stack=new Stack<>();
    Stack<Integer> min=new Stack<>();
    public void push(int node) {
        stack.push(node);
        if(min.isEmpty()||min.peek()>=node){
            min.push(node);
        }else{
            min.push(min.peek());
        }
    }
     
    public void pop() {
        if(stack.isEmpty()&&min.isEmpty()){
            return;
        }
        stack.pop();
        min.pop();
    }
     
    public int top() {
        if(stack.isEmpty()&&min.isEmpty()){
            return -1;
        }
        return stack.peek();
    }
     
    public int min() {
        if(stack.isEmpty()&&min.isEmpty()){
            return -1;
        }
        return min.peek();
    }
}
```



###### 21.栈的压入、弹出序列

​	输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否可能为该栈的弹出顺序。假设压入栈的所有数字均不相等。例如序列1,2,3,4,5是某栈的压入顺序，序列4,5,3,2,1是该压栈序列对应的一个弹出序列，但4,3,5,1,2就不可能是该压栈序列的弹出序列。（注意：这两个序列的长度是相等的）

```java
public class Solution {
    public boolean IsPopOrder(int [] pushA,int [] popA) {
        int index=0;
      Stack<Integer> stack = new Stack<>();
        for(int i=0;i<pushA.length;i++){
            stack.push(pushA[i]);
            while(!stack.isEmpty()&&(stack.peek()==popA[index])){
                stack.pop();
                index++;
            }
        }
        return stack.isEmpty();
    }
}
```



###### 22.从上往下打印二叉树

​	从上往下打印出二叉树的每个节点，同层节点从左至右打印。

```java
public class Solution {
    public ArrayList<Integer> PrintFromTopToBottom(TreeNode root) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(root==null){
            return res;
        }
        Queue<TreeNode> queue=new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            int size=queue.size();
            for(int i=0;i<size;i++){
                TreeNode node=queue.poll();
                res.add(node.val);
                if(node.left!=null){
                    queue.add(node.left);
                }
                if(node.right!=null){
                    queue.add(node.right);
                }
            }
        }
        return res;
    }
}
```



###### 23.二叉搜索树的后序遍历序列

​	输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。如果是则返回true,否则返回false。假设输入的数组的任意两个数字都互不相同。

```java
public class Solution {
    public boolean VerifySquenceOfBST(int [] sequence) {
        int len=sequence.length;
        if(len==0){
            return false;
        }
        return help(sequence,0,len-1);
    }
     
    private boolean help(int[] sequence,int l,int r){
        if(l>=r){
            return true;
        }
        int key=sequence[r];
        int i=l;
        for(;i<r;i++){
            if(sequence[i]>key){
                break;
            }
        }
        for(int j=i;j<r;j++){
            if(sequence[j]<key){
                return false;
            }
        }
        return help(sequence,l,i-1)&&help(sequence,i+1,r);
    }
}
```



###### 24.二叉树中和为某一值的路径

​	输入一颗二叉树的根节点和一个整数，按字典序打印出二叉树中结点值的和为输入整数的所有路径。路径定义为从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。

```java
public class Solution {
    ArrayList<ArrayList<Integer>> res=new ArrayList<>();
    ArrayList<Integer> list = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> FindPath(TreeNode root,int target) {
        if(root==null){
            return res;
        }
        help(root,target);
        return res;
    }
     
    private void help(TreeNode root,int target){
        if(root==null){
            return;
        }
        target-=root.val;
        list.add(root.val);
        if(root.left==null&&root.right==null&&target==0){
            res.add(new ArrayList<>(list));
        }
        help(root.left,target);
        help(root.right,target);
        list.remove(list.size()-1);
    }
}

```



###### 25.复杂链表的复制

​	输入一个复杂链表（每个节点中有节点值，以及两个指针，一个指向下一个节点，另一个特殊指针random指向一个随机节点），请对此链表进行深拷贝，并返回拷贝后的头结点。（注意，输出结果中请不要返回参数中的节点引用，否则判题程序会直接返回空）

```java
public class Solution {
    public RandomListNode Clone(RandomListNode head)
    {
        if(head==null){
            return head;
        }
        Map<RandomListNode,RandomListNode>map=new HashMap<>();
        RandomListNode cur=head;
        while(cur!=null){
            map.put(cur,new RandomListNode(cur.label));
            cur=cur.next;
        }
        cur=head;
        while(cur!=null){
            RandomListNode p=map.get(cur);
            p.next=map.get(cur.next);
            p.random=map.get(cur.random);
            cur=cur.next;
        }
        return map.get(head);
    }
}

```

