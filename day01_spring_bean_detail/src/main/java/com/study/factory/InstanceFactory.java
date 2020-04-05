package com.study.factory;

import com.study.service.IAccountService;
import com.study.service.impl.AccountServiceImpl;

/**
 * 模拟一个工厂类，该类可能存在于jar包中，我们无法通过修改源码的方式提供默认构造函数
 */
public class InstanceFactory {
    public IAccountService getAccountService(){
        return new AccountServiceImpl();
    }
}
