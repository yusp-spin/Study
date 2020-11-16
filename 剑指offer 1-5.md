---
title: 剑指offer 1-5
date: 2020-09-27 9:48:47
categories: 算法
tags: 剑指offer
---

###### 1.  二维数组中的查找

在一个二维数组中（每个一维数组的长度相同），每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。

```java
public class Solution {
    public boolean Find(int target, int [][] array) {
        int m = array.length;
        if(m == 0) {
            return false;
        }
        int n = array[0].length;
        int i = m - 1;
        int j = 0;
        while (i >= 0 && j < n) {
            if (array[i][j] == target) {
                return true;
            } else if(array[i][j] > target) {
                i--;
            } else {
                j++;
            }
        }
        return false;
    }
}

```
###### 2. 替换空格

请实现一个函数，将一个字符串中的每个空格替换成“%20”。例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。

```java
public class Solution {
    public String replaceSpace(StringBuffer str) {
        String s = str.toString();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                sb.append("%20");
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
}
```

###### 3. 从尾到头打印链表

输入一个链表，按链表从尾到头的顺序返回一个ArrayList。

```java
public class Solution {
    public ArrayList<Integer> printListFromTailToHead(ListNode head) {
        ArrayList<Integer> res = new ArrayList<>();
        while (head != null) {
            res.add(0,head.val);
            head = head.next;
        }
        return res;
    }
}
```

###### 4. 重建二叉树

输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建二叉树并返回。

```java
public class Solution {
    public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        if (pre.length == 0) {
            return null;
        }
        int key = pre[0];
        if (pre.length != in.length) {
            return  null;
        }
        int len = pre.length;
        int i = 0;
        for (; i < len; i++) {
            if (in[i] == key) {
                break;
            }
        }
        TreeNode root = new TreeNode(key);
        root.left = reConstructBinaryTree(Arrays.copyOfRange(pre, 1, i + 1), 
                                        Arrays.copyOfRange(in, 0, i));
        root.right=reConstructBinaryTree(Arrays.copyOfRange(pre, i + 1, len),
                                        Arrays.copyOfRange(in, i + 1, len));
        return root;
    }
}
```

###### 5. 用两个栈实现队列

用两个栈来实现一个队列，完成队列的Push和Pop操作。 队列中的元素为int类型。

```java
public class Solution {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();
    public void push(int node) {
        stack1.push(node);
    }
    public int pop() {
        if(!stack2.isEmpty()){
            return stack2.pop();
        }else{
            while(!stack1.isEmpty()){
                stack2.push(stack1.pop());
            }
            return stack2.pop();
        }
    }
}
```

