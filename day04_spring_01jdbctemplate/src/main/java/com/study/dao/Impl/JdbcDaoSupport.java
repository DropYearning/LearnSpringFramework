//package com.study.dao.Impl;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//import javax.xml.crypto.Data;
//
///**
// * 此类用于抽取dao实现类中的重复代码
// */
//public class JdbcDaoSupport {
//    private JdbcTemplate jdbcTemplate; //由Spring注入
//
//    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public JdbcTemplate getJdbcTemplate() {
//        return jdbcTemplate;
//    }
//
//
//    public void setDataSource(DataSource dataSource) {
//        if(jdbcTemplate == null){
//            jdbcTemplate = createJdbcTemplate(dataSource);
//        }
//    }
//
//    private JdbcTemplate createJdbcTemplate(DataSource dataSource){
//        return new JdbcTemplate(dataSource);
//    }
//}
