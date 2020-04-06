package com.study.proxy;

/**
 * 一个生产者
 */
public class Producer implements IProducer{

    // 销售
    public void saleProduct(float money){
        System.out.println("生产者销售产品，并拿到钱：" + money);
    }

    // 售后服务
    public void afterService(float money){
        System.out.println("生产者提供售后服务，并拿到钱：" + money);
    }
}
