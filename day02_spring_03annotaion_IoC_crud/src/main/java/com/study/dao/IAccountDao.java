package com.study.dao;

import com.study.domain.Account;

import java.util.List;

/**
 * 账户的持久层接口
 */
public interface IAccountDao {

    List<Account> findAllAccount(); // 查询所有

    Account findAccountById(Integer id); // 查询1个

    void saveAccount(Account account);

    void updateAccount(Account account);

    void deleteAccount(Integer id);

}
