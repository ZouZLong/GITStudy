package com.example.myapplication.aaa_study;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式学习
 * 五种不同的单例模式
 */
public class SingleCase {
    private volatile static SingleCase singleCase = null;


    //懒汉式单例 synchronized(线程安全模式)线程锁，力度太大
    public static synchronized SingleCase getInstance1() {
        if (singleCase == null) {
            singleCase = new SingleCase();
        }
        return singleCase;
    }

    //懒汉式单例 优化后--》双重校验DCL 力度减小  非常常见的单例模式
    public static SingleCase getInstance3() {
        if (singleCase == null) {//第一次检查
            synchronized (SingleCase.class) {
                if (singleCase == null) {//第二次检查
                    singleCase = new SingleCase();
                }
            }
        }
        return singleCase;
        //步骤：
        //1.singleCase实例化分配对象
        //2.调用SingleCase构造方法，初始化成员字段
        //3.SingleCase对象赋值给singleCase
        //JDK 可以是乱序的 导致双重检查失效
        //JDK1.5之后多了一个关键字 volatile 可以防止乱序
    }

    //饿汉式单例模式  线程安全  初始化类的时候直接就创建好了单例模式
    static class SingleCase1 {
        private static SingleCase1 singleCase1 = new SingleCase1();

        private SingleCase1() {
        }

        public static SingleCase1 getInstance() {
            return singleCase1;
        }
    }

    //静态内部类单例  线程安全
    static class SingleCase2 {
        private SingleCase2() {
        }

        private static class SingleCase_1 {
            protected static final SingleCase2 SINGLE_CASE_2 = new SingleCase2();
        }

        public static SingleCase2 getInstance() { //延迟加载，调用的时候才会实例化
            return SingleCase_1.SINGLE_CASE_2;
        }
    }


    //使用容器
    static class SingleCaseManager {
        private static Map<String, Object> map = new HashMap<>();

        public static void registerService(String key, Object instance) {
            if (!map.containsKey(key)) {
                map.put(key, instance);
            }
        }

        public static Object getService(String key) {
            return map.get(key);
        }
    }
}
