package com.study.dao.impl;

import com.study.dao.IAccountDao;

/**
 * 账户的持久层实现类
 */
public class AccountDaoImpl implements IAccountDao {
    public void saveAccount() {
        System.out.println("模拟保存成功");
    }
}
