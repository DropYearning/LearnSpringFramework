package com.study.service;

import com.study.domain.Account;

import java.util.List;

/**
 * 账户的业务层接口
 */
public interface IAccountService {

    List<Account> findAllAccount(); // 查询所有

    Account findAccountById(Integer accountId); // 查询1个

    void saveAccount(Account account);

    void updateAccount(Account account);

    void deleteAccount(Integer accountId);


}
