package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.dao.impl.AccountDaoImpl;
import com.study.service.IAccountService;

/**
 * 账户的业务层实现类
 */
public class AccountServiceImpl implements IAccountService {
    private IAccountDao accountDao = new AccountDaoImpl();
    // 这里有一个小细节，此处accountDao是直接new出来的，也就是说在创建AccountServiceImpl对象时会首先new一个AccountDaoImpl对象
    // 如果改为：private IAccountDao accountDao ;运行，会报NPE异常
    //private IAccountDao accountDao = (IAccountDao) BeanFactory.getBean("accountDaoImpl"); //通过工厂解藕

    public AccountServiceImpl(){
        System.out.println("AccountServiceImpl对象已创建");
    }
    public void  saveAccount(){
        accountDao.saveAccount();
    }
}
