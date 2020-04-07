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

    Account findAccountByName(String accountName); // 根据名称查询账户，如果结果唯一就返回，如果没有结果就返回null
        // 如果有超过1个结果就抛异常

}
