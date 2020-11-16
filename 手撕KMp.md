---
title: 手撕KMP
date: 2020-09-19 11:22:47
categories: 算法
tags: KMP
---

之前看到一个好的KMP理解方法，记录一下

参考：https://mp.weixin.qq.com/s?__biz=Mzg2NzA4MTkxNQ==&mid=2247485979&idx=2&sn=56d4d0dd11951c29c9e6f94803d92e03&scene=21#wechat_redirect

```java
package com.CSDN;

public class KMP {
    private int[][] dp;
    private String pat;
    public KMP(String pat){
        this.pat=pat;
        int m=pat.length();
        dp=new int[m][256];
        dp[0][pat.charAt(0)]=1;
        int X=0;
        for(int j=1;j<m;j++){
            for(int c=0;c<256;c++){
                if(pat.charAt(j)==c){
                    dp[j][c]=j+1;
                }else{
                    dp[j][c]=dp[X][c];
                }
            }
            X=dp[X][j];
        }
    }

    public int serach(String txt){
        int m=pat.length();
        int n=txt.length();
        int j=0;
        for (int i = 0; i < n; i++) {
            j=dp[j][txt.charAt(i)];
            if(j==m){
                return i-m+1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        KMP K=new KMP("bcja");
        System.out.println(K.serach("abcjdsbcjahfksd"));

    }
}
```