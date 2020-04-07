# LearnSpringFramework-day3-Spring-使用AOP改进之前的account事务操作(XML)
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## AOP改进事务操作
```xml
<!--开始配置AOP-->
<aop:config>
    <!--被各个aspect引用的pointcut表达式必须放在所有的aspect之前-->
    <aop:pointcut id="accountServiceImplPt" expression="execution(* com.study.service.impl.*.*(..))"/>

    <!--配置切面-->
    <aop:aspect id="txAdvice" ref="txManager">
        <!--配置前置通知：beginTransaction-->
        <aop:before method="beginTransaction" pointcut="execution(* com.study.service.impl.*.*(..))"></aop:before>
        <!--配置后置通知：commit-->
        <aop:after-returning method="commit" pointcut="execution(* com.study.service.impl.*.*(..))"></aop:after-returning>
        <!--配置异常通知：rollback-->
        <aop:after-throwing method="rollback" pointcut-ref="accountServiceImplPt"></aop:after-throwing>
        <!--配置最终通知：release-->
        <aop:after method="release" pointcut-ref="accountServiceImplPt"></aop:after>
    </aop:aspect>
</aop:config>
```


