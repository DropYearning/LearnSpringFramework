# LearnSpringFramework-day3-Spring-改进之前的account账户操作例子
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## 增加转账方法
- A账户给B账户赚钱，示例的写法如下：
```
public void transfer(String sourceName, String targetName, float money) {
        // 1 根据名称查询转出账户余额
        Account source = accountDao.findAccountByName(sourceName);
        // 2 根据名称查询转入账户
        Account target = accountDao.findAccountByName(targetName);
        // 3 转出账户减钱
        source.setMoney(source.getMoney() - money);
        // 4 转入账户加钱
        target.setMoney(target.getMoney() + money);
        // 5 更新转出账户
        accountDao.updateAccount(source);
        int i = 1/0; // 故意抛出异常
        // 6 更新转入账户
        accountDao.updateAccount(target);
    }
```
- 其中若出现了异常（ int i = 1/0; ）会出现转出方扣钱，转入方没收到钱的问题，为什么？ —— 尽管此时是有事务机制的（因为没有机制就不可能commit成功造成金额的改变），但是我们
- ![7aFjy5Y](https://i.imgur.com/7aFjy5Y.png)
- 可以让所有的accountDao使用同一个connection：
    - **需要使用`ThreadLocal`，对象把Connection和当前线程绑定，从而使一个线程中只有一个能控制事务的对象**。

> 事务控制应该都在业务层

## 问题出在哪？
为什么转账会失败， 转出方扣钱了，但是转入方没有收到钱
我们查看bean.xml文件
```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 配置Service Bean -->
    <bean id="accountService" class="com.study.service.impl.AccountServiceImpl">
        <!-- 注入dao -->
        <property name="accountDao" ref="accountDao"></property>
    </bean>

    <!--配置Dao对象 Bean-->
    <bean id="accountDao" class="com.study.dao.impl.AccountDaoImpl">
        <!-- 注入QueryRunner -->
        <property name="runner" ref="runner"></property>
    </bean>

    <!--配置QueryRunner Bean-->
        <!--配置QueryRunner配置成多例-->
    <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
        <!--注入数据源-->
        <!--使用构造参数注入数据源对象-->
        <constructor-arg name="ds" ref="dataSource"></constructor-arg>
    </bean>

    <!-- 配置数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <!--连接数据库的必备信息-->
        <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/eesy?useSSL=true&amp;serverTimezone=GMT&amp;useUnicode=true&amp;characterEncoding=utf-8"></property>
        <property name="user" value="root"></property>
        <property name="password" value="12345678"></property>
    </bean>
</beans>
```
从bean.xml中我们可以看到，我们将`AccountServiceImpl`实例存入了Spring的容器，`AccountServiceImpl`实例内的`accountDao`也是由Spring注入的。层层递进，`accountDao`中也存在由Spring负责注入的`QueryRunner runner`，`QueryRunner runner`中又存在由Spring负责注入(使用构造函数注入的)的`DataSource ds`。当我们在调用`as.transfer`方法时，在tansfer()方法内一共调用了accountDao的四个方法findAccountByName, findAccountByName, setMoney, setMoney。这四个方法的事务是交给userDao中的QueryRunner控制的, 而QueryRunner的数据库连接来自于Spring负责注入的ds, **该ds连接上的事务是默认自动提交的**。 **因此，这四个方法的事务操作是分开的、单独进行提交的。** 所以当`int i = 1/0;`抛出异常后 `accountDao.updateAccount(target);`的事务没有得到提交，而前面三个事务都已经提交了。**所以，为了解决上面的事务问题，我们希望手动控制事务的提交操作。**
我们通过新增`ConnectionUtils`和`TransactionManager`这两个类来实现事务的手动控制。


## 增加事务控制工具类
- ConnectionUtils：
```java
/**
 * 连接的工具类，用于从数据源中获取一个连接并且实现和线程的绑定
 */
public class ConnectionUtils {

    private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
    private DataSource dataSource; // 由Spring注入

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
```
- TransactionManager：
```java
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

    // 开启事务
    public void beginTransaction(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 提交事务
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 回滚事务
    public void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 释放连接
    public void release(){
        try {
            connectionUtils.getThreadConnection().close(); // 将连接还回连接池
            connectionUtils.removeConnection(); // 把连接和线程解绑
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 修改AccountDaoImpl.java
修改前：使用的是`query(String sql, ResultSetHandler<T> rsh)`这个方法使用的是从数据源（数据池）中得到的连接来执行SQL语句的。
![Q2VdYK](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/Q2VdYK.png)

```java
public class AccountDaoImpl implements IAccountDao {

    private QueryRunner runner;

    // 让spring提供QueryRunner
    public void setRunner(QueryRunner runner) {
        this.runner = runner;
    }

    @Override
    public List<Account> findAllAccount() {
        try {
        
            return runner.query("select * from account", new BeanListHandler<Account>(Account.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    ... ...
}
```
修改后: 使用的是`query(Connection conn, String sql, ResultSetHandler<T> rsh)`这个方法执行的SQL语句，用的是我们传入的`connectionUtils.getThreadConnection()`得到的数据源连接来执行SQL。

```java
public class AccountDaoImpl implements IAccountDao {

    private QueryRunner runner;
    private ConnectionUtils connectionUtils;

    // 让spring提供QueryRunner
       public void setRunner(QueryRunner runner) {
        this.runner = runner;
    }

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    @Override
    public List<Account> findAllAccount() {
        try {
            // 在每一个方法中使用connectionUtils.getThreadConnection()得到的连接来执行SQL操作
            return runner.query(connectionUtils.getThreadConnection(),"select * from account", new BeanListHandler<Account>(Account.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

## 修改bean.xml
修改AccountDaoImpl.java之后，我们不仅需要在accountDao中注入QueryRunner runner，还需要注入ConnectionUtils connectionUtils用于获取当前线程上的连接。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 配置Service Bean -->
    <bean id="accountService" class="com.study.service.impl.AccountServiceImpl">
        <!-- 注入dao -->
        <property name="accountDao" ref="accountDao"></property>
    </bean>

    <!--配置Dao对象 Bean-->
    <bean id="accountDao" class="com.study.dao.impl.AccountDaoImpl">
        <!-- 注入QueryRunner -->
        <property name="runner" ref="runner"></property>
        <!--注入ConnectionUtils-->
        <property name="connectionUtils" ref="connectionUtils"></property>
    </bean>

    <!--配置QueryRunner Bean-->
    <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
        <!--这里不再需要没有带线程绑定的数据源了, 我们不在需要使用QueryRunner来获取Datasource connection-->
    </bean>

    <!-- 配置数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <!--连接数据库的必备信息-->
        <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/eesy?useSSL=true&amp;serverTimezone=GMT&amp;useUnicode=true&amp;characterEncoding=utf-8"></property>
        <property name="user" value="root"></property>
        <property name="password" value="12345678"></property>
    </bean>

    <!--配置connectionUtils工具类-->
    <bean id="connectionUtils" class="com.study.utils.ConnectionUtils">
        <!--注入数据源-->
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!--配置TransactionUtils工具类-->
    <bean id="txManager" class="com.study.utils.TransactionManager">
        <!--注入ConnectionUtils-->
        <property name="connectionUtils" ref="connectionUtils"></property>
    </bean>
</beans>
```

- 完整的事务过程：
```
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
```
1. 使用`txManager.beginTransaction();`开启事务，这一步做的操作是：为当前执行tansfer()方法的当前线程绑定上一个数据源连接对象，并将这个数据源连接对象的自动事务提交设置为false
2. 之后使用accountDao对象执行四次操作用的都是当前线程上绑定的同一个数据源连接对象，并且每一次小的执行都不会自动提交事务。只有全体操作都运行成功之后，才会commit。
3. 当遇到`int i = 1/0;`异常时，这个未提交事务的连接上所做的所有操作都被回滚。


> 注意：函数中声明的返回值在try语句中返回了的，应该在catch语句中使用throw抛出错误


## 反思
- 添加了事务控制之后的转账操作虽然不会再出现功能上错误，但是却在AccountServiceImpl中添加了很多的事务控制相关的重复代码。
    ```java
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
    ```
- 且各个Bean之间的依赖关系十分复杂

## 再改进：使用动态代理创建带有事务管理的AccountServiceImpl对象.
- 增加一个工厂类BeanFactory，用于创建**带有事务管理的AccountServiceImpl**对象.
- BeanFactory依赖于原始的AccountServiceImpl类和TransactionManager类，两者都通过Spring注入BeanFactory，因此需要提供2者的set方法
- BeanFactory类中通过getAccountService()方法返回一个带有事务控制的IAccountService对象，这里为了增强，使用了JDK动态代理。
    - 旧的不带事务控制的getAccountService():
        ```java
        public  IAccountService getAccountService(){
              return this.accountService;
          }  
        ```
    - 使用了动态代理带事务控制的getAccountService()：
        ```java
        public  IAccountService getAccountService(){
                return (IAccountService)Proxy.newProxyInstance(accountService.getClass().getClassLoader(), accountService.getClass().getInterfaces(), new InvocationHandler() {      
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
        ```
- 对应bean.xml中修改
    ```xml
      <!--配置代理的service对象(支持事务控制)-->
        <bean id="proxyAccountService" factory-bean="beanFactory" factory-method="getAccountService"></bean>
    
        <!--配置beanfactory-->
        <bean id="beanFactory" class="com.study.factory.BeanFactory">
            <property name="accountService" ref="accountService"></property>
            <property name="txManager" ref="txManager"></property>
        </bean>
    ```
- 使用动态代理之后，去除了实现事务控制的繁冗代码。
- 但是增加了xml配置上的麻烦和使用动态代理的麻烦
- 有没有更好的方法？——Spring AOP
