# LearnSpringFramework-day3-使用Spring内置的事务控制接口-基于XML

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)



## 基于XML的声明式事务控制的XML头

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">
```



## Spring中基于XML的声明式事务控制配置步骤
- 1、配置事务管理器
    ```xml
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    ```
- 2、配置事务的通知
    - 需要导入事务的约束(tx的名称空间和约束\AOP)
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    https://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/tx
    https://www.springframework.org/schema/tx/spring-tx.xsd
    http://www.springframework.org/schema/aop
    https://www.springframework.org/schema/aop/spring-aop.xsd">
    ```
    - 使用<tx:advice>标签配置事务通知
        - 属性id：给事务通知起一个唯一标志
        - transaction-manage: 给事务通知提供一个事务管理器引用
 - 3、配置AOP中的通用切入点表达式
    ```xml
    <!--配置公共的切入点表达式-->
    <aop:pointcut id="pt1" expression="execution(* com.study.service.impl.*.*(..))"/>
    ```
 - 4、建立事务通知和切入点表达式的关系
    ```xml
     <!--建立切入点表达式和事务通知之间的关系-->
    <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"></aop:advisor>
   ```
 - 5、配置事务的属性：是在事务的通知tx:advice标签的内部配置。
    - **isolation**:用于指定事务的隔离级别，默认值是DEFAULT,表示使用数据库的默认隔离级别
    - **propagation** ：用于指定事务的传播行为，默认值是REQUIRED，表示一定会有事务，增删改的选择。查询方法可以选择SUPPORTS。
    - **read-only**：用于指定事务是否只读，只有查询方法才能设置为true，默认值是false,表示读写
    - **no-rollback-for**：用于指定一个异常，当产生该异常时事务不回滚；产生其他异常时事务回滚；没有默认值，表示任何异常都回滚
    - **rollback-for**：用于指定一个异常，当产生该异常时事务回滚；产生其他异常时事务不回滚；没有默认值，表示任何异常都回滚。
    - **timeout**：用于指定事务的超时时间，默认值是-1，表示永不超时，如果指定了数值，以秒为单位

## 完整的XML配置文件例子
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">
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

    <!--Spring中基于XML的声明式事务控制配置步骤-->
    <!--1、 配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--2、 配置事务的通知：需要导入事务的约束(tx的名称空间和约束)-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <!--配置事务的属性-->
        <tx:attributes>
            <tx:method name="transfer" propagation="REQUIRED" read-only="false"/>
            <!--通过方法名的通配来指定所有查询方法的事务属性-->
            <tx:method name="find*" propagation="SUPPORTS" read-only="true"></tx:method>
        </tx:attributes>
    </tx:advice>
    <!--3、配置AOP-->
    <aop:config>
        <!--配置公共的切入点表达式-->
        <aop:pointcut id="pt1" expression="execution(* com.study.service.impl.*.*(..))"/>
        <!--建立切入点表达式和事务通知之间的关系-->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"></aop:advisor>
    </aop:config>
</beans>
```





