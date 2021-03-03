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

## 事务控制工具类
- ConnectionUtils：
- TransactionManager：
- 完整的事务过程：
```
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
```

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

## 使用动态代理再改进
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
