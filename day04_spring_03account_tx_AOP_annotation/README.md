# LearnSpringFramework-day3-Spring-使用AOP改进之前的account事务操作(注解)
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## Spring中基于注解 的声明式事务控制配置步骤
1. 配置Spring创建容器是要扫描的包`<context:component-scan base-package="com.study"></context:component-scan>`
2. 配置事务管理器 `org.springframework.jdbc.datasource.DataSourceTransactionManager`
3. 开启spring对注解事务的支持 `<tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>`
4. 在需要事务支持的地方使用@Transactional注解


## 注解AOP的XML配置头
```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!--配置Spring创建容器是要扫描的包-->
    <context:component-scan base-package="com.study"></context:component-scan>

    <!--配置jdbcTemplate-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/eesy?useUnicode=true&amp;characterEncoding=utf-8"></property>
        <property name="username" value="root"></property>
        <property name="password" value="12345678"></property>
    </bean>

    <!--Spring中基于注解的声明式事务控制配置步骤-->
    <!--1、配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!--2、开启Spring对注解事务的支持-->
    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>

</beans>
```

## 为AccountServiceImpl添加事务注解
```java
/**
 * 账户的业务层实现类
 * 事务控制应该都在业务层
 */
@Service("accountService")
@Transactional
public class AccountServiceImpl implements IAccountService {

    // 业务层需要调用持久层
    @Autowired
    private IAccountDao accountDao;

    @Override
    public Account findAccountById(Integer accountId) {
       return accountDao.findAccountById(accountId);
    }

    @Override
    public void transfer(String sourceName, String targetName, float money) {
        System.out.println("transfer开始...");
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
        //int i = 1/0;
            // 2.6 更新转入账户
        accountDao.updateAccount(target);
    }
}
```

## 事务注解的出现位置
- `@Transactional`注解可以出现在接口上，类上和方法上。
  - 出现接口上，表示该接口的所有实现类都有事务支持。
  - 出现在类上，表示类中所有方法有事务支持
  - 出现在方法上，表示方法有事务支持。
- 以上三个位置的优先级：方法>类>接口

## 注解实现事务控制的问题
- Spring在用注解开发AOP的时候会先调用最终通知，然后再调用后置通知。调用最终通知的conn已经关闭，在之后调用后置通知的时候当然无法commit了
- ![ISirzSe](https://i.imgur.com/ISirzSe.png)

## 使用环绕通知解决问题
```java
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
```

