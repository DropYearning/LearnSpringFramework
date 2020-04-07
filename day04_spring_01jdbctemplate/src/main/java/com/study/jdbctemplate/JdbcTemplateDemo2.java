package com.study.jdbctemplate;

import com.study.domain.Account;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBCTemplate的CRUD
 */
public class JdbcTemplateDemo2 {
    public static void main(String[] args) {
        // 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 获取bean对象
        JdbcTemplate jt = ac.getBean("jdbcTemplate", JdbcTemplate.class);
        // 执行操作
        // 保存
        //jt.update("insert into account (name, money)values (?,?)", "eee", 3333f);
        // 更新
        //jt.update("update  account set name=?, money=? where id =?", "update", 33f, 7);
        // 删除
        //jt.update("delete from account where id=?", 7);
        //查询所有
        //List<Account> accounts = jt.query("select * from account where money > ?", new AccountRowMapper(),1000f);
        //List<Account> accounts = jt.query("select * from account where money > ?", new BeanPropertyRowMapper<Account>(Account.class),1000f);
        //for (Account a:accounts){
        //    System.out.println(a);
        //}
        //查询一个
        //List<Account> accounts = jt.query("select * from account where id=?", new BeanPropertyRowMapper<Account>(Account.class),1);
        //System.out.println(accounts.isEmpty()?"没有结果":accounts.get(0));

        //查询返回一列一行（使用聚合函数，但不加group by字句）
        Integer count = jt.queryForObject("select count(*) from account where money > ?",Integer.class, 1000f);
        System.out.println(count);
    }
}

// 定义Account的封装策略
class AccountRowMapper implements RowMapper<Account>{
    @Override
    // 把结果集中的数据封装到Account中，然后由Spring把每一个Account加到集合中
    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getInt("id"));
        account.setName(resultSet.getNString("name"));
        account.setMoney(resultSet.getFloat("money"));
        return account;
    }
}
