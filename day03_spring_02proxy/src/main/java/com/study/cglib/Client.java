package com.study.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 模拟一个消费者
 */
public class Client {
    public static void main(String[] args) {
        Producer producer = new Producer();

        /**
         * 基于子类的动态代理
         * public Object create(Class[] argumentTypes, Object[] arguments)
         */

        Producer cglibProducer = (Producer)Enhancer.create(producer.getClass(), new MethodInterceptor() {
            /**
             * intercept：执行被代理对象的任何方法都会经过该方法
             * @param o(proxy):代理对象的引用
             * @param method:当前执行的方法
             * @param args:当前执行方法所用的参数
             * @param methodProxy：当前执行方法的代理对象
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
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

        cglibProducer.saleProduct(10000f); // cglib经销商卖电脑
    }
}
