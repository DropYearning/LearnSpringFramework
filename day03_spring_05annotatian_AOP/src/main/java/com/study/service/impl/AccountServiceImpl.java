package com.study.service.impl;

import com.study.service.IAccountService;
import org.springframework.stereotype.Service;

/**
 * 账户的业务层实现类
 */
@Service("accountService")
public class AccountServiceImpl implements IAccountService {
    @Override
    public void saveAccount() {
        System.out.println("执行了AccountServiceImpl保存方法");
    }

    @Override
    public void updateAccount(int i) {
        System.out.println("执行了AccountServiceImpl更新方法 " + i);
    }

    @Override
    public int deleteAccount() {
        System.out.println("执行了AccountServiceImpl删除方法");
        return 0;
    }
}
