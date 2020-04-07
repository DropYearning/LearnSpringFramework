# LearnSpringFramework-day3-Spring-使用AOP改进之前的account事务操作(注解)
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)


## 注解AOP的XML配置头
```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!--配置Spring创建容器时要扫描的包-->
    <context:component-scan base-package="com.study"></context:component-scan>
    
    <!--配置Spring开启注解AOP的支持-->
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>

</beans>
```

## 配置Spring创建容器时要扫描的包
`<context:component-scan base-package="com.study"></context:component-scan>`

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

