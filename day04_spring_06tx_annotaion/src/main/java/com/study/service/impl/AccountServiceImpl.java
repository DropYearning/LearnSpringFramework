package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import com.study.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 账户的业务层实现类
 * 事务控制应该都在业务层
 */
@Service("accountService")
@Transactional
public class AccountServiceImpl implements IAccountService {

    // 业务层需要调用持久层
    @Autowired
    private IAccountDao accountDao;



    @Override
    public Account findAccountById(Integer accountId) {
       return accountDao.findAccountById(accountId);
    }



    /**
     * 这里的转账操作只是示意，实际业务中肯定不能这样写
     * @param sourceName
     * @param targetName
     * @param money
     */
    @Override
    public void transfer(String sourceName, String targetName, float money) {
        System.out.println("transfer开始...");
            // 2.1 根据名称查询转出账户余额
        Account source = accountDao.findAccountByName(sourceName);
            // 2.2 根据名称查询转入账户
        Account target = accountDao.findAccountByName(targetName);
            // 2.3 转出账户减钱
        source.setMoney(source.getMoney() - money);
            // 2.4 转入账户加钱
        target.setMoney(target.getMoney() + money);
            // 2.5 更新转出账户
        accountDao.updateAccount(source);
        //int i = 1/0;
            // 2.6 更新转入账户
        accountDao.updateAccount(target);
    }
}
