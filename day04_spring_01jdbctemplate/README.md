# LearnSpringFramework-day3-Spring-JDBCTemplate

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## Spring JDBCTemplate
- ![LdWhBCR](https://i.imgur.com/LdWhBCR.jpg)
- Spring的JDBCTemplate在作用层次上和hibernate/Mybatis/dbutil在同一个层次，对底层的JDBC进行了薄薄的封装
- Spring提供了很多的操作模板，操作关系型数据的：JdbcTemplate、HibernateTemplate；操作 nosql 数据库的：RedisTemplate，操作消息队列的：JmsTemplate
- JdbcTemplate的作用：它就是用于和数据库交互的，实现对表的CRUD操作
- 



## JDBCTemplate需要的Maven依赖
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
```

## JdbcTemplateDemo1:最基本的使用
```java
public class JdbcTemplateDemo1 {
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
```
## Spring Template的CRUD
- jt.update("insert into account (name, money)values (?,?)", "eee", 3333f); // 保存
- jt.update("update  account set name=?, money=? where id =?", "update", 33f, 7); // 更新
- jt.update("delete from account where id=?", 7); // 删除
- 查询操作：jt.query("select * from account where money > ?", new AccountRowMapper(),1000f);
    - 指定封装的方法一：需要定义一个实现了RowMapper接口的类AccountRowMapper,`List<Account> accounts = jt.query("select * from account where money > ?", new AccountRowMapper(),1000f);`
    ```java
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
    ```
    - 指定封装的方法二：使用Spring提供的BeanPropertyRowMapper,`List<Account> accounts = jt.query("select * from account where money > ?", new BeanPropertyRowMapper<Account>(Account.class),1000f);`
- 查询一个：`List<Account> accounts = jt.query("select * from account where id=?", new BeanPropertyRowMapper<Account>(Account.class),1); `
- 



## 注意
- 注意：在.java程序中书写jdbc URL时不需要使用&amp;来转义&；在.xml中中书写jdbc URL时需要使用&amp;来转义&；在.properties文件中书写时也不需要使用&amp;来转义&

## 改进
- 若有多个DaoImpl，获取JdbcTemplate的代码需要重复写多遍
- 改进：抽取出dao实现类中的重复代码写到JdbcDaoSupport类中，其他dao继承自JdbcDaoSupport类，即可继承父类中的成员和方法
- 再改进：可以直接继承自Spring提供的JdbcSupport类[org.springframework.jdbc.core.support.JdbcDaoSupport]，省去获取JdbcTemplate的代码
```java
/**
 * 账户的持久层实现类
 */
public class AccountDaoImpl extends JdbcDaoSupport implements IAccountDao {

    @Override
    public Account findAccountById(Integer accountId) {
        List<Account> accounts = getJdbcTemplate().query("select * from account where id = ?", new BeanPropertyRowMapper<Account>(Account.class), accountId);
        return accounts.isEmpty()?null:accounts.get(0);
    }

    @Override
    public Account findAccountByName(String accountName) {
        List<Account> accounts =  getJdbcTemplate().query("select * from account where name = ?", new BeanPropertyRowMapper<Account>(Account.class), accountName);
        if (accounts.isEmpty()){
            return null;
        }
        if (accounts.size()>1){
            throw new RuntimeException("查询结果不唯一");
        }
        return accounts.get(0);
    }

    @Override
    public void updateAccount(Account account) {
        getJdbcTemplate().update("update account set name=?, money=? where id=?", account.getName(), account.getMoney(), account.getId());
    }
}
```