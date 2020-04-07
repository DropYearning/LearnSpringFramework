package com.study.jdbctemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * JDBCTemplate的最基本用法，数据源配置写在代码里
 */
public class JdbcTemplateDemo {
    public static void main(String[] args) {
        // 准备数据源（Spring的内置数据源）
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/eesy?useUnicode=true&characterEncoding=utf-8");
        ds.setUsername("root");
        ds.setPassword("12345678");

        // 1 创建JDBCtemplate对象
        JdbcTemplate jt = new JdbcTemplate();
        // 给jt设置数据源
        jt.setDataSource(ds);
        // 2 执行操作
        jt.execute("insert  into account (name, money)values('ccc', 1000)");

    }
}
