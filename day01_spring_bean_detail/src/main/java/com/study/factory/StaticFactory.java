package com.study.factory;

import com.study.service.IAccountService;
import com.study.service.impl.AccountServiceImpl;

/**
 * 模拟一个静态工厂类
 */
public class StaticFactory {
    public static IAccountService getAccountService(){
        return new AccountServiceImpl();
    }
}
