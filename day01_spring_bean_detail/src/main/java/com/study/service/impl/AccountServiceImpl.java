package com.study.service.impl;

import com.study.service.IAccountService;

/**
 * 账户的业务层实现类
 */
public class AccountServiceImpl implements IAccountService {


    // 增加参数后相当于没有了默认的无参数构造函数
    // Failed to instantiate [com.study.service.impl.AccountServiceImpl]: No default constructor found
    public AccountServiceImpl(){
        System.out.println("AccountServiceImpl对象已创建");
    }

    public void  saveAccount(){
        System.out.println("AccountServiceImpl中的saveAccount方法执行了");
    }

    public void init(){
        System.out.println("AccountServiceImpl对象初始化了");
    }
    public void destroy(){
        System.out.println("AccountServiceImpl对象销毁了");
    }
}
