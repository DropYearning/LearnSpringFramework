package com.study.dao.impl;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * 持久层实现类
 */
@Repository("accountDao")
public class AccountDaoImpl implements IAccountDao {

    @Autowired
    private QueryRunner runner;

    //// 让spring提供QueryRunner
    //public void setRunner(QueryRunner runner) {
    //    this.runner = runner;
    //}

    @Override
    public List<Account> findAllAccount() {
        try {
            return runner.query("select * from account", new BeanListHandler<Account>(Account.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account findAccountById(Integer id) {
        try {
            return runner.query("select * from account where id = ?", new BeanHandler<Account>(Account.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveAccount(Account account) {
        try {
             runner.update("insert into account (name, money) values(?, ?)", account.getName(), account.getMoney());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAccount(Account account) {
        try {
            runner.update("update account set name=?, money=? where id = ?", account.getName(), account.getMoney(), account.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAccount(Integer id) {
        try {
            runner.update("delete from account where id = ?", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
