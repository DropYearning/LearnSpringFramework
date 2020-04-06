package com.study.cglib;

import com.study.proxy.IProducer;

/**
 * 一个生产者(Cglib代理不需要被代理类实现任何接口)
 */
public class Producer  {

    // 销售
    public void saleProduct(float money){
        System.out.println("生产者销售产品，并拿到钱：" + money);
    }

    // 售后服务
    public void afterService(float money){
        System.out.println("生产者提供售后服务，并拿到钱：" + money);
    }
}
