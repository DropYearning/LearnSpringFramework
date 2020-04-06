package com.study.dao.impl;

import com.study.dao.IAccountDao;
import org.springframework.stereotype.Repository;

/**
 * 账户的持久层实现类
 */
@Repository("accountDao2")
public class AccountDaoImpl2 implements IAccountDao {
    public AccountDaoImpl2() {
        System.out.println("AccountDaoImpl2对象被创建了");
    }

    public void saveAccount() {
        System.out.println("模拟2保存成功");
    }
}
