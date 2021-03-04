# LearnSpringFramework-day3-使用Spring内置的事务控制接口-环境配置

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## Spring内置的事务控制
- JavaEE 体系进行分层开发，**事务处理位于业务层**，Spring 提供了分层设计业务层的事务处理解决方案。
- Spring 框架为我们提供了一组事务控制的接口。这组接口是在spring-tx-5.0.2.RELEASE.jar 中的。
- Spring 的事务控制都是基于 AOP 的，它既可以使用编程的方式实现，也可以使用配置的方式实现。我们学习的重点是使用配置的方式实现。
### Spring 中事务控制的 API 介绍
#### PlatformTransactionManager:  Spring的事务管理器
此接口是 spring 的事务管理器，它里面提供了我们常用的操作事务的方法，如下图：
![X6wEBv](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/X6wEBv.png)
- `org.springframework.jdbc.datasource.DataSourceTransactionManager` ： 使用 Spring JDBC 或 iBatis 进行持久化数据时使用
- `org.springframework.orm.hibernate5.HibernateTransactionManager` ：使用 Hibernate 版本进行持久化数据时使用

#### TransactionDefinition: 事务的定义信息对象
它是事务的定义信息对象，里面有如下方法：
![4teD2F](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/4teD2F.png)
![bMZjwu](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/bMZjwu.png)
- 事务的传播行为:
  - REQUIRED:如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。一般的选择（默认值）
  - SUPPORTS:支持当前事务，如果当前没有事务，就以非事务方式执行（没有事务）
  - MANDATORY：使用当前的事务，如果当前没有事务，就抛出异常
  - REQUERS_NEW:新建事务，如果当前在事务中，把当前事务挂起。
  - NOT_SUPPORTED:以非事务方式执行操作，如果当前存在事务，就把当前事务挂起
  - NEVER:以非事务方式运行，如果当前存在事务，抛出异常
  - NESTED:如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行 REQUIRED 类似的操作。
- 超时时间: 默认值是-1，没有超时限制。如果有，以秒为单位进行设置。
- 是否是只读事务: 建议查询时设置为只读
#### TransactionStatus: 提供事务具体的运行状态
此接口提供的是事务具体的运行状态
![42bxqM](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/42bxqM.png)


## Maven依赖

```xml
<packaging>jar</packaging>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.19</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <!--解析切入点表达式用-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.7</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
    </dependencies>
```





## XML头

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--配置业务层-->
    <bean id="accountService" class="com.study.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"></property>
    </bean>

    <!--配置账户的持久层-->
    <bean id="accountDao" class="com.study.dao.Impl.AccountDaoImpl">
        <property name="dataSource" ref="dataSource"></property>
    </bean>


    <!--配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/eesy?useUnicode=true&amp;characterEncoding=utf-8"></property>
        <property name="username" value="root"></property>
        <property name="password" value="12345678"></property>
    </bean>
</beans>
```

