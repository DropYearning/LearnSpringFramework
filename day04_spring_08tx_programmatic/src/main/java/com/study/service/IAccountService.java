package com.study.service;

import com.study.domain.Account;

/**
 * 账户的业务层接口
 */
public interface IAccountService {
    Account findAccountById(Integer accountId);

    void transfer(String sourceName, String targetName, float money);
}
