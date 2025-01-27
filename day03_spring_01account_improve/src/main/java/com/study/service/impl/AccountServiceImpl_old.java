package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.domain.Account;
import com.study.service.IAccountService;
import com.study.utils.TransactionManager;

import java.util.List;

/**
 * 账户的业务层实现类
 * 事务控制应该都在业务层
 */
public class AccountServiceImpl_old implements IAccountService {

    // 业务层需要调用持久层
    private IAccountDao accountDao;
    private TransactionManager txManager;

    public void setTxManager(TransactionManager txManager) {
        this.txManager = txManager;
    }

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<Account> findAllAccount() {
        try{
            // 1 开启事务
            txManager.beginTransaction();
            // 2 执行操作
            List<Account> accounts = accountDao.findAllAccount();
            // 3 提交事务
            txManager.commit();
            // 4返回结果
            return accounts;
        }catch (Exception e){
            //回滚操作
            txManager.rollback();
            throw new RuntimeException("findAllAccount出错");
        }finally {
            // 释放连接
            txManager.release();
        }
    }

    @Override
    public Account findAccountById(Integer accountId) {
        try{
            // 1 开启事务
            txManager.beginTransaction();
            // 2 执行操作
            Account account = accountDao.findAccountById(accountId);
            // 3 提交事务
            txManager.commit();
            // 4返回结果
            return account;
        }catch (Exception e){
            //回滚操作
            txManager.rollback();
            throw new RuntimeException("findAccountById出错");
        }finally {
            // 释放连接
            txManager.release();
        }
    }

    @Override
    public void saveAccount(Account account) {
        try{
            // 1 开启事务
            txManager.beginTransaction();
            // 2 执行操作
            accountDao.saveAccount(account);
            // 3 提交事务
            txManager.commit();
        }catch (Exception e){
            //回滚操作
            txManager.rollback();
            throw new RuntimeException("saveAccount出错");
        }finally {
            // 释放连接
            txManager.release();
        }
    }

    @Override
    public void updateAccount(Account account) {
        try{
            // 1 开启事务
            txManager.beginTransaction();
            // 2 执行操作
            accountDao.updateAccount(account);
            // 3 提交事务
            txManager.commit();
        }catch (Exception e){
            //回滚操作
            txManager.rollback();
            throw new RuntimeException("findAllAccount出错");
        }finally {
            // 释放连接
            txManager.release();
        }
    }

    @Override
    public void deleteAccount(Integer accountId) {
        try{
            // 1 开启事务
            txManager.beginTransaction();
            // 2 执行操作
            accountDao.deleteAccount(accountId);
            // 3 提交事务
            txManager.commit();
        }catch (Exception e){
            //回滚操作
            txManager.rollback();
            throw new RuntimeException("findAllAccount出错");
        }finally {
            // 释放连接
            txManager.release();
        }
    }

    /**
     * 这里的转账操作只是示意，实际业务中肯定不能这样写
     * @param sourceName
     * @param targetName
     * @param money
     */
    @Override
    public void transfer(String sourceName, String targetName, float money) {
        try{
            // 1 开启事务
            txManager.beginTransaction();
            // 2 执行操作
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
            int i = 1/0;
                // 2.6 更新转入账户
            accountDao.updateAccount(target);
            // 3 提交事务
            txManager.commit();
        }catch (Exception e){
            //回滚操作
            txManager.rollback();
            e.printStackTrace(); // 有事务处理之后，抛出除0异常后之前发生的转账操作会回滚
            throw new RuntimeException("findAllAccount出错");
        }finally {
            // 释放连接
            txManager.release();
        }

    }
}
