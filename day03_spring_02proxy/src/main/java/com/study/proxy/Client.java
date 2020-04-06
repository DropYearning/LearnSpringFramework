package com.study.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 模拟一个消费者
 */
public class Client {
    public static void main(String[] args) {
        // 匿名内部类访问外部变量，需要其是final的
        final Producer producer = new Producer();
        //producer.saleProduct(10000f); // 厂家直销

        /**
         *动态代理:InvocationHandler 用于提供**增强**的代码。**一般都是填入一个该接口的实现类，通常使用匿名内部类。**此接口的实现类都是谁用谁来写
         *  - invoke方法中含有下面三个参数：
         *         - proxy:代理对象的引用
         *         - method：当前执行的方法
         *         - args：当前执行方法所用的参数
         *         - 返回值: 和被代理对象有相同的返回值
         */
        // 创建一个代理对象proxyProducer
        IProducer proxyProducer = (IProducer)Proxy.newProxyInstance(producer.getClass().getClassLoader(), producer.getClass().getInterfaces(), new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //return  method.invoke(producer, args); // 最简单的代理——什么都不增强，纯粹直接调用被代理类的方法

                Object returnValue = null; // 返回值
                // 提供增强的代码（比如在这里由经销商proxy提取20%的佣金）
                // 1 获取方法执行的参数
                Float money = (Float) args[0];
                // 2 判断当前方法是不是销售方法
                if ("saleProduct".equals(method.getName())){
                    returnValue = method.invoke(producer, money*0.8f); // 经销商(proxy)再调用被代理的方法将剩余的销售额转给工厂
                }
                return  returnValue;
            }
        });
        proxyProducer.saleProduct(10000f); // 经销商(proxy)在卖电脑
    }
}
