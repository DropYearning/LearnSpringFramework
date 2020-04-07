package com.study.factory;

import com.study.service.IAccountService;
import com.study.utils.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 用于创建Service的代理对象的工厂
 */
public class BeanFactory {
    private  IAccountService accountService;
    private TransactionManager txManager;

    public void setTxManager(TransactionManager txManager) {
        this.txManager = txManager;
    }

    // set用于xml反射获取实例，需要加final
    public final void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    // BeanFactory中获取Service的代理对象的方法, 返回的是一个增强了的AccountServiceImpl的代理类
    public  IAccountService getAccountService(){
        return (IAccountService)Proxy.newProxyInstance(accountService.getClass().getClassLoader(), accountService.getClass().getInterfaces(), new InvocationHandler() {
            //添加事务的支持
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object rtValue = null;
                try{
                    // 1 开启事务
                    txManager.beginTransaction();
                    // 2 执行操作
                    rtValue = method.invoke(accountService, args);
                    // 3 提交事务
                    txManager.commit();
                    // 4返回结果
                    return rtValue;
                }catch (Exception e){
                    //回滚操作
                    txManager.rollback();
                    throw new RuntimeException("findAllAccount出错");
                }finally {
                    // 释放连接
                    txManager.release();
                }
            }
        });
    }
}
