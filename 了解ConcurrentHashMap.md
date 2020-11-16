---
title: 了解ConcurrentHashMap
date: 2020-11-15 10:09:01
categories: 数据结构
---

1. ##### ConcurrentHashMap中没有负载因子和阈值吗
	
	是没有，改用了sizeCtl控制，0表示默认值，-1表示正在扩容，>0表示下一次扩容的门槛，-（1+nThreads）n个线程正在扩容，sizeCtl的变化都是CAS操作
	
	sizeCtl：
	
	（1）-1，表示有线程正在进行初始化操作
	
	（2）-(1 + nThreads)，表示有n个线程正在一起扩容
	
	（3）0，默认值，后续在真正初始化的时候使用默认容量
	
	（4）> 0，初始化或扩容完成后下一次的扩容门槛
	
	```java
	public ConcurrentHashMap() {
	}
	
	public ConcurrentHashMap(int initialCapacity) {
	    if (initialCapacity < 0)
	        throw new IllegalArgumentException();
	    int cap = ((initialCapacity >= (MAXIMUM_CAPACITY >>> 1)) ?
	            MAXIMUM_CAPACITY :
	            tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1));
	    this.sizeCtl = cap;
	}
	
	public ConcurrentHashMap(Map<? extends K, ? extends V> m) {
	    this.sizeCtl = DEFAULT_CAPACITY;
	    putAll(m);
	}
	
	public ConcurrentHashMap(int initialCapacity, float loadFactor) {
	    this(initialCapacity, loadFactor, 1);
	}
	
	public ConcurrentHashMap(int initialCapacity,
	                         float loadFactor, int concurrencyLevel) {
	    if (!(loadFactor > 0.0f) || initialCapacity < 0 || concurrencyLevel <= 0)
	        throw new IllegalArgumentException();
	    if (initialCapacity < concurrencyLevel)   // Use at least as many bins
	        initialCapacity = concurrencyLevel;   // as estimated threads
	    long size = (long)(1.0 + (long)initialCapacity / loadFactor);
	    int cap = (size >= (long)MAXIMUM_CAPACITY) ?
	            MAXIMUM_CAPACITY : tableSizeFor((int)size);
	    this.sizeCtl = cap;
	}
	```
	
	
	
2. ##### 请讲讲ConcurrentHashMap的put操作？
	
	a. 控制key和value都不能为null
	b. 再用自旋+cas实现put过程，下面是具体的put
	c. 如果桶未初始化就初始化桶
	d. 如果桶中还没有元素就把这个元素插进去，插入这一步就是采用的CAS
	e. 如果要插入的桶正在扩容迁移元素，就当前线程一起帮忙协助扩容
	f. 如果桶已经存在且没有迁移元素
	g. 就锁住这个桶，是采用分段锁的思想（锁住后里面不需要cas）
	h. 如果元素存在就替换，不存在size+1，插到链表或者树的尾部
	i. 如果桶的元素个数达到了8就尝试树化
	j. 元素个数+1（分段锁思想），同时检查是否需要扩容
k. 返回
	
	```java
	 /**
	     * Maps the specified key to the specified value in this table.
	     * Neither the key nor the value can be null.
	     *
	     * <p>The value can be retrieved by calling the {@code get} method
	     * with a key that is equal to the original key.
	     *
	     * @param key key with which the specified value is to be associated
	     * @param value value to be associated with the specified key
	     * @return the previous value associated with {@code key}, or
	     *         {@code null} if there was no mapping for {@code key}
	     * @throws NullPointerException if the specified key or value is null
	     */
	    public V put(K key, V value) {
	        return putVal(key, value, false);
	    }
	
	    /** Implementation for put and putIfAbsent */
	    final V putVal(K key, V value, boolean onlyIfAbsent) {
	        if (key == null || value == null) throw new NullPointerException();
	        int hash = spread(key.hashCode());
	        int binCount = 0;
	        for (Node<K,V>[] tab = table;;) {
	            Node<K,V> f; int n, i, fh;
	            if (tab == null || (n = tab.length) == 0)
	                tab = initTable();
	            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
	                if (casTabAt(tab, i, null,
	                             new Node<K,V>(hash, key, value, null)))
	                    break;                   // no lock when adding to empty bin
	            }
	            else if ((fh = f.hash) == MOVED)
	                tab = helpTransfer(tab, f);
	            else {
	                V oldVal = null;
	                synchronized (f) {
	                    if (tabAt(tab, i) == f) {
	                        if (fh >= 0) {
	                            binCount = 1;
	                            for (Node<K,V> e = f;; ++binCount) {
	                                K ek;
	                                if (e.hash == hash &&
	                                    ((ek = e.key) == key ||
	                                     (ek != null && key.equals(ek)))) {
	                                    oldVal = e.val;
	                                    if (!onlyIfAbsent)
	                                        e.val = value;
	                                    break;
	                                }
	                                Node<K,V> pred = e;
	                                if ((e = e.next) == null) {
	                                    pred.next = new Node<K,V>(hash, key,
	                                                              value, null);
	                                    break;
	                                }
	                            }
	                        }
	                        else if (f instanceof TreeBin) {
	                            Node<K,V> p;
	                            binCount = 2;
	                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
	                                                           value)) != null) {
	                                oldVal = p.val;
	                                if (!onlyIfAbsent)
	                                    p.val = value;
	                            }
	                        }
	                    }
	                }
	                if (binCount != 0) {
	                    if (binCount >= TREEIFY_THRESHOLD)
	                        treeifyBin(tab, i);
	                    if (oldVal != null)
	                        return oldVal;
	                    break;
	                }
	            }
	        }
	        addCount(1L, binCount);
	        return null;
	    }
	```
	
	
	
3. ##### 请讲讲ConcurrentHashMap的扩容操作？
	
	初始化桶操作：判断是否正在扩容，正在的话就让出cpu。否则就用CAS更新sizeCtl（CAS控制只有一个线程初始化桶数组），设置容量，并设置扩容门槛（写死的）赋值给sizeCtl
	
	addCount操作，元素数量+1，并会判断是否需要扩容，这里是数组数量采用分段锁的思想，根据不同线程存储到不同段上, 先尝试把数量加到baseCount上，如果失败再加到分段的CounterCell上（采用CAS添加）,如果不同线程对应的分段都添加失败，就要扩容段
	
	扩容：sizeCtl存储着扩容门槛，高位存储扩容邮戳，低位存储存储着扩容线程数加1，即1+nThreads),sc小于0，说明正在扩容，就当前线程加入到迁移元素中去，扩容线程+1；如果没有线程在扩容，自己就扩容，加入迁移元素，sizeCtl低位为2（1+nThreads）
	
	协助扩容：线程添加元素时发现正在扩容且当前元素所在的桶元素已经迁移完成了，则协助迁移其它桶的元素。迁移完成的标志：如果桶数组不为空，并且当前桶第一个元素为ForwardingNode类型，并且nextTab不为空
	
	迁移元素：扩容时容量变为两倍，并把部分元素迁移到其它桶中。迁移元素先从靠后的桶开始；迁移完成的桶在里面放置一ForwardingNode类型的元素，标记该桶迁移完成；
	
	过程：迁移元素是用sychronized锁住该桶，迁移的时候，根据迁移时根据hash&n是否等于0把桶中元素分化成两个链表或树；等于0的在原来的位置，不等于0 就在原来的位置+n,(n的扩大的大小)
	
	添加元素：
	
	```java
	 /**
	     * Adds to count, and if table is too small and not already
	     * resizing, initiates transfer. If already resizing, helps
	     * perform transfer if work is available.  Rechecks occupancy
	     * after a transfer to see if another resize is already needed
	     * because resizings are lagging additions.
	     *
	     * @param x the count to add
	     * @param check if <0, don't check resize, if <= 1 only check if uncontended
	     */
	    private final void addCount(long x, int check) {
	        CounterCell[] as; long b, s;
	        if ((as = counterCells) != null ||
	            !U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x)) {
	            CounterCell a; long v; int m;
	            boolean uncontended = true;
	            if (as == null || (m = as.length - 1) < 0 ||
	                (a = as[ThreadLocalRandom.getProbe() & m]) == null ||
	                !(uncontended =
	                  U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))) {
	                fullAddCount(x, uncontended);
	                return;
	            }
	            if (check <= 1)
	                return;
	            s = sumCount();
	        }
	        if (check >= 0) {
	            Node<K,V>[] tab, nt; int n, sc;
	            while (s >= (long)(sc = sizeCtl) && (tab = table) != null &&
	                   (n = tab.length) < MAXIMUM_CAPACITY) {
	                int rs = resizeStamp(n);
	                if (sc < 0) {
	                    if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
	                        sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||
	                        transferIndex <= 0)
	                        break;
	                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
	                        transfer(tab, nt);
	                }
	                else if (U.compareAndSwapInt(this, SIZECTL, sc,
	                                             (rs << RESIZE_STAMP_SHIFT) + 2))
	                    transfer(tab, null);
	                s = sumCount();
	            }
	        }
	    }
	
	```
	
	协助扩容：
	
	```java
	/**
	     * Helps transfer if a resize is in progress.
	     */
	    final Node<K,V>[] helpTransfer(Node<K,V>[] tab, Node<K,V> f) {
	        Node<K,V>[] nextTab; int sc;
	        if (tab != null && (f instanceof ForwardingNode) &&
	            (nextTab = ((ForwardingNode<K,V>)f).nextTable) != null) {
	            int rs = resizeStamp(tab.length);
	            while (nextTab == nextTable && table == tab &&
	                   (sc = sizeCtl) < 0) {
	                if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
	                    sc == rs + MAX_RESIZERS || transferIndex <= 0)
	                    break;
	                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
	                    transfer(tab, nextTab);
	                    break;
	                }
	            }
	            return nextTab;
	        }
	        return table;
	    }
	
	```
	
	迁移元素：
	
	```java
	/**
	     * Moves and/or copies the nodes in each bin to new table. See
	     * above for explanation.
	     */
	    private final void transfer(Node<K,V>[] tab, Node<K,V>[] nextTab) {
	        int n = tab.length, stride;
	        if ((stride = (NCPU > 1) ? (n >>> 3) / NCPU : n) < MIN_TRANSFER_STRIDE)
	            stride = MIN_TRANSFER_STRIDE; // subdivide range
	        if (nextTab == null) {            // initiating
	            try {
	                @SuppressWarnings("unchecked")
	                Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n << 1];
	                nextTab = nt;
	            } catch (Throwable ex) {      // try to cope with OOME
	                sizeCtl = Integer.MAX_VALUE;
	                return;
	            }
	            nextTable = nextTab;
	            transferIndex = n;
	        }
	        int nextn = nextTab.length;
	        ForwardingNode<K,V> fwd = new ForwardingNode<K,V>(nextTab);
	        boolean advance = true;
	        boolean finishing = false; // to ensure sweep before committing nextTab
	        for (int i = 0, bound = 0;;) {
	            Node<K,V> f; int fh;
	            while (advance) {
	                int nextIndex, nextBound;
	                if (--i >= bound || finishing)
	                    advance = false;
	                else if ((nextIndex = transferIndex) <= 0) {
	                    i = -1;
	                    advance = false;
	                }
	                else if (U.compareAndSwapInt
	                         (this, TRANSFERINDEX, nextIndex,
	                          nextBound = (nextIndex > stride ?
	                                       nextIndex - stride : 0))) {
	                    bound = nextBound;
	                    i = nextIndex - 1;
	                    advance = false;
	                }
	            }
	            if (i < 0 || i >= n || i + n >= nextn) {
	                int sc;
	                if (finishing) {
	                    nextTable = null;
	                    table = nextTab;
	                    sizeCtl = (n << 1) - (n >>> 1);
	                    return;
	                }
	                if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
	                    if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)
	                        return;
	                    finishing = advance = true;
	                    i = n; // recheck before commit
	                }
	            }
	            else if ((f = tabAt(tab, i)) == null)
	                advance = casTabAt(tab, i, null, fwd);
	            else if ((fh = f.hash) == MOVED)
	                advance = true; // already processed
	            else {
	                synchronized (f) {
	                    if (tabAt(tab, i) == f) {
	                        Node<K,V> ln, hn;
	                        if (fh >= 0) {
	                            int runBit = fh & n;
	                            Node<K,V> lastRun = f;
	                            for (Node<K,V> p = f.next; p != null; p = p.next) {
	                                int b = p.hash & n;
	                                if (b != runBit) {
	                                    runBit = b;
	                                    lastRun = p;
	                                }
	                            }
	                            if (runBit == 0) {
	                                ln = lastRun;
	                                hn = null;
	                            }
	                            else {
	                                hn = lastRun;
	                                ln = null;
	                            }
	                            for (Node<K,V> p = f; p != lastRun; p = p.next) {
	                                int ph = p.hash; K pk = p.key; V pv = p.val;
	                                if ((ph & n) == 0)
	                                    ln = new Node<K,V>(ph, pk, pv, ln);
	                                else
	                                    hn = new Node<K,V>(ph, pk, pv, hn);
	                            }
	                            setTabAt(nextTab, i, ln);
	                            setTabAt(nextTab, i + n, hn);
	                            setTabAt(tab, i, fwd);
	                            advance = true;
	                        }
	                        else if (f instanceof TreeBin) {
	                            TreeBin<K,V> t = (TreeBin<K,V>)f;
	                            TreeNode<K,V> lo = null, loTail = null;
	                            TreeNode<K,V> hi = null, hiTail = null;
	                            int lc = 0, hc = 0;
	                            for (Node<K,V> e = t.first; e != null; e = e.next) {
	                                int h = e.hash;
	                                TreeNode<K,V> p = new TreeNode<K,V>
	                                    (h, e.key, e.val, null, null);
	                                if ((h & n) == 0) {
	                                    if ((p.prev = loTail) == null)
	                                        lo = p;
	                                    else
	                                        loTail.next = p;
	                                    loTail = p;
	                                    ++lc;
	                                }
	                                else {
	                                    if ((p.prev = hiTail) == null)
	                                        hi = p;
	                                    else
	                                        hiTail.next = p;
	                                    hiTail = p;
	                                    ++hc;
	                                }
	                            }
	                            ln = (lc <= UNTREEIFY_THRESHOLD) ? untreeify(lo) :
	                                (hc != 0) ? new TreeBin<K,V>(lo) : t;
	                            hn = (hc <= UNTREEIFY_THRESHOLD) ? untreeify(hi) :
	                                (lc != 0) ? new TreeBin<K,V>(hi) : t;
	                            setTabAt(nextTab, i, ln);
	                            setTabAt(nextTab, i + n, hn);
	                            setTabAt(tab, i, fwd);
	                            advance = true;
	                        }
	                    }
	                }
	            }
	        }
	    }
	```
	
	
	
4. ##### 删除流程：

  a. 删除元素跟添加元素一样，都是先找到元素所在的桶，然后采用分段锁的思想锁住整个桶，再进行操作。
  b. 流程：进入自旋，如果正在扩容，协助扩容，如果没有，对桶加锁，找到元素，删除，如果是树退化成链表，还要进行退化操作，删除了元素，就元素个数-1；返回旧值，没有删除元素就返回null

5. ##### 获取元素：

  a. 不加锁，
  b. 所以ConcurrentHashMap不是强一致性的‘

6. ##### Jdk1.7和1.8的区别，底层数据结构有什么不同，put和get操作的不同

   a. 数据结构差别：JDK1.7中ConcurrentHashMap采用了数组+Segment+分段锁，segment继承自ReentrantLock，缺点：需要两次hash，hash时间长。优点：并发能力高，segment之间相互不影响；JDK1.8中，数组+链表+红黑树的实现方式来设计，内部大量采用CAS操作
   b. 数据结构：取消了Segment分段锁的数据结构，取而代之的是数组+链表+红黑树的结构。
   c. 保证线程安全机制：JDK1.7采用segment的分段锁机制实现线程安全，其中segment继承自ReentrantLock。JDK1.8采用CAS+Synchronized保证线程安全。
   d. 锁的粒度：原来是对需要进行数据操作的Segment加锁，现调整为对每个数组元素加锁（Node）。
   e. 链表转化为红黑树:定位结点的hash算法简化会带来弊端,Hash冲突加剧,因此在链表节点数量大于8时，会将链表转化为红黑树进行存储。
   f. 查询时间复杂度：从原来的遍历链表O(n)，变成遍历红黑树O(logN)。







