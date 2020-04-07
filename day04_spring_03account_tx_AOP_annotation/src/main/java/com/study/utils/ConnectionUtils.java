package com.study.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 连接的工具类，用于从数据源中获取一个连接并且实现和线程的绑定
 */
@Component("connectionUtils")
public class ConnectionUtils {

    private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();

    @Autowired
    private DataSource dataSource; // 由Spring注入


    // 获取当前线程上连接
    public Connection getThreadConnection(){
        // 1 先从ThreadLocal上获取
        Connection conn = tl.get();
        try {
            // 2 判断当前线程上是否有连接
            if (conn == null){
                // 3 从数据源中获取一个连接，并且和线程绑定，存入ThreadLocal当中
                conn = dataSource.getConnection();
                // 4 把conn存入ThreadLocal
                tl.set(conn);
            }
            // 返回当前线程上的连接
            return  conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 把连接和线程解绑
    public void removeConnection(){
        tl.remove();
    }

}
