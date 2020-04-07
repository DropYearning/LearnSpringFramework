# LearnSpringFramework-day3-Spring-基于注解的AOP配置

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](

## 基于注解的AOP XML头
```xml
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

## 配置步骤
- 配置Spring创建容器时要扫描的包：`<context:component-scan base-package="com.study"></context:component-scan>`
- **@Aspect** ：表示当前类是一个切面类。其中的各个通知方法也需要用对应的标签标注通知类型。
    - @Before()：前置通知
    - @AfterReturning()：后置通知
    - @AfterThrowing：异常通知
    - @After()：最终通知
    - @Around():环绕通知 
- 可以在Logger类中配置可被引用的切入点表达式：之后可以再其他各个方法中使用pt1来引用这一段切入点表达式`execution(* com.study.service.impl.*.*(..))`
    ```
    @Pointcut("execution(* com.study.service.impl.*.*(..))")
    private void pt1(){}
    ```
    - 引用的方式：
        ```
        @Before("pt1()")
        public void beforePrintLog(){
            System.out.println("前置通知：Logger类中的前置通知");
        }
        ```
- 配置Spring开启注解AOP的支持:`<aop:aspectj-autoproxy></aop:aspectj-autoproxy>`

## 存在的问题
- 低版本的Spring在使用注解配置AOP时会出现通知方法的调用顺序问题
    - 注意下面的最终通知和后置通知的顺序：
    - ![mcx1ONR](https://i.imgur.com/mcx1ONR.png)
- 使用环绕通知不会出现通知顺序的问题

## 完全不使用XML的开发方式
- **@EnableAspectJAutoProxy** : 添加在Spring配置类前可以替代`<aop:aspectj-autoproxy></aop:aspectj-autoproxy>`
![I1x8EXv](https://i.imgur.com/I1x8EXv.png)