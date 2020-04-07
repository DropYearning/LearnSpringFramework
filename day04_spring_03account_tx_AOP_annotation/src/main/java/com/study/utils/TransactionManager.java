package com.study.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * 事务管理工具类, 包含：
 *  开启事务、提交事务、回滚事务和释放连接
 */

@Component("txManager")
@Aspect
public class TransactionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    // 配置公共的切入点表达式
    @Pointcut("execution(* com.study.service.impl.*.*(..))")
    private void pt1(){}


    // 开启事务（前置通知）
    //@Before("pt1()")
    public void beginTransaction(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 提交事务（后置通知）
    //@AfterReturning("pt1()")
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 回滚事务（异常通知）
    //@AfterThrowing("pt1()")
    public void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 释放连接（最终通知）
    //@After("pt1()")
    public void release(){
        try {
            connectionUtils.getThreadConnection().close(); // 将连接还回连接池
            connectionUtils.removeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Around("pt1()")
    // 环绕通知方法（解决通知顺序异常的问题）
    public Object aroundAdvice(ProceedingJoinPoint pjp){
        Object rtValue = null;
        try{
            // 获取参数
            Object[] args = pjp.getArgs();
            // 开启事务（前置通知）
            this.beginTransaction();
            // 执行转账方法
            rtValue = pjp.proceed(args);
            // // 提交事务（后置通知）
            this.commit();
            return  rtValue;
        }catch (Throwable e){
            // 回滚事务（异常通知）
            this.rollback();
            throw new RuntimeException(e);
        }finally {
            // 释放连接（最终通知）
            this.release();
        }
    }

}
