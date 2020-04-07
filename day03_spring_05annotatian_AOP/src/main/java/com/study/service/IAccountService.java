package com.study.service;

/**
 * 账户的业务层接口
 */
public interface IAccountService {
    void saveAccount(); // 模拟保存账户（无返回值，无参数）

    void updateAccount(int i); // 模拟更新某个账户（无返回值，有参数）

    int deleteAccount();// 删除账户（有返回值，无参数）

}
