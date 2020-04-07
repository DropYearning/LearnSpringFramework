package com.study.utils;

import java.sql.SQLException;

/**
 * 事务管理工具类, 包含：
 *  开启事务、提交事务、回滚事务和释放连接
 */
public class TransactionManager {

    private ConnectionUtils connectionUtils;

    // 等待Spring注入
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    // 开启事务（前置通知）
    public void beginTransaction(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 提交事务（后置通知）
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 回滚事务（异常通知）
    public void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 释放连接（最终通知）
    public void release(){
        try {
            connectionUtils.getThreadConnection().close(); // 将连接还回连接池
            connectionUtils.removeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
