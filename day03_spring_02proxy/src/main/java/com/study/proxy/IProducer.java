package com.study.proxy;

/**
 * 对生产厂家行为的抽象接口
 */
public interface IProducer {
    public void saleProduct(float money);

    // 售后服务
    public void afterService(float money);
}
