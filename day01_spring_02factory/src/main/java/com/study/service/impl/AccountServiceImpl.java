package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.dao.impl.AccountDaoImpl;
import com.study.factory.BeanFactory;
import com.study.service.IAccountService;

/**
 * 账户的业务层实现类
 */
public class AccountServiceImpl implements IAccountService {
    //private IAccountDao accountDao = new AccountDaoImpl();
    //private IAccountDao accountDao = (IAccountDao) BeanFactory.getBean("accountDaoImpl"); //通过工厂解藕

    public void  saveAccount(){
        IAccountDao accountDao = (IAccountDao) BeanFactory.getBean("accountDaoImpl"); //通过工厂解藕
            // 这里的accountDao若在函数外作为类的成员变量初始化会报NPE
        int i = 1;
        accountDao.saveAccount();
        System.out.println(i);
        i++;
    }
}
