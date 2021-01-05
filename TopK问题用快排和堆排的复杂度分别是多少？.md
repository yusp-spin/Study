---
title: TopK问题用快排和堆排的复杂度分别是多少？
date: 2020-12-21 10:22:56
categories: 算法
tags: TopK
---

1. ##### 关于TopK问题

   TopK问题就是在一堆数据里面找到前 K 大(当然也可以是前 K 小)的数

2. ##### 常规方法，完全排序

   先完全排序后取topK，这种方法需要将数据完全排序，不适用于大数据量

3. ##### 利用快排

   ###### 3.1 解决思路

   借鉴快排的思想，在patiton中数组会分为三个部分，我们只要与index相比较就可以得出TopK是在左边部分还是右边部分，因此不需要全部排序

   ```java
   public class Solution {
       //要求的第k个数
       int k;
       List<Integer> res = new ArrayList<>();
       public List<Integer> GetLeastNumbers(int [] input, int k) {
           if (input.length < k || k < 1) {
               return res;
           }
           this.k = k;
           quick_sort(input, 0, input.length - 1);
           return res;
       }
   
       private  void quick_sort(int[] arr, int l, int r) {
           if(l > r) {
               return ;
           }
           int index = patiton(arr, l, r);
           //此处判断就是为了变成局部排序
           if(index == k - 1) {
               for (int i = 0; i <= index; i++) {
                   res.add(arr[i]);
               }
               return;
           }else if(index >= k) {
               quick_sort(arr, l ,index - 1);
           }else {
               quick_sort(arr, index + 1, r);
           }
       }
   
       private int patiton(int[] arr, int l, int r) {
           int tmp=arr[l];
           while(l<r){
               while(l<r&&arr[r]>=tmp){
                   r--;
               }
               arr[l]=arr[r];
               while (l<r&&arr[l]<=tmp){
                   l++;
               }
               arr[r]=arr[l];
           }
           arr[l]=tmp;
           return l;
       }
   }
   ```

   ###### 3.2 复杂度分析

   ​	与快排完全排序不同，每次分割后的数组大小近似为原数组大小的一半，因此这种方法的时间复杂度实际上是O(N)+O(N/2)+O(N/4)+……＜O(2N)，因此时间复杂度为**O(N)**，但是这种方法需要提前将N个数读入，对于海量数据来说，对空间开销很大

4. ##### 堆排序法

   ###### 4.1 解决思路

   ​	先随机取出N个数中的K个数，将这N个数构造为小顶堆，那么堆顶的数肯定就是这K个数中最小的数了，然后再将剩下的N-K个数与堆顶进行比较，如果大于堆顶，那么说明该数有机会成为TopK，就更新堆顶为该数，此时由于小顶堆的性质可能被破坏，就还需要调整堆

   ```java
   public class Solution {
       ArrayList<Integer> res = new ArrayList<>();
       public ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
           if (input.length < k || k < 1) {
               return res;
           }
           ArrayList<Integer> res = new ArrayList<>();
           PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
               public int compare(Integer a, Integer b) {
                   return b - a;
               }
           });
           int len = input.length;
           if(len < k || k < 1) {
               return res;
           }
           for(int i = 0; i < len; i++) {
               if(queue.size() < k) {
                   queue.add(input[i]);
               }else {
                   if(queue.peek() > input[i]) {
                       queue.poll();
                       queue.add(input[i]);
                   }
                   
               }
           }
           return new ArrayList<>(queue);
       }
   }
   ```

   ###### 4.2 复杂度分析

   ​	首先需要对K个元素进行建堆，时间复杂度为O(k)，然后要遍历数组,最坏的情况是，每个元素都与堆顶比较并排序，需要堆化n次 所以是O(nlog(k))，因此总复杂度是**O(k+nlog(k))**;

   ##### 5.小结

   ​	快排和堆排解决topK问题的复杂度其实面试中有被问过，有不少面试者直接答的快排和堆排的复杂度，但其实是不一样的，在TopK问题中，快排和堆排的复杂度分别问**O(n)和O(k+nlog(k))**

   ​	通过对比可以发现，堆排的优势是只需读入K个数据即可，可以实现来一个数据更新一次，能够很好的实现数据动态读入并找出TopK。