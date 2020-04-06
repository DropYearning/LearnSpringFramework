package com.study.dao.impl;

import com.study.dao.IAccountDao;
import org.springframework.stereotype.Repository;

/**
 * 账户的持久层实现类
 */
@Repository("accountDao1")
public class AccountDaoImpl implements IAccountDao {
    public AccountDaoImpl() {
        System.out.println("AccountDaoImpl1对象被创建了");
    }

    public void saveAccount() {
        System.out.println("模拟1保存成功");
    }
}
