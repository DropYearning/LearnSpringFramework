# LearnSpringFramework-day3-Spring-AOP中通知advice的类型

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](


## Spring中基于XML的AOP配置步骤
- 1 把通知的Bean(Logger)也交给Spring管理
- 2 使用aop:config标签表明开始AOP的配置
- 3 使用aop:aspect标签表明开始配置切面
    - id属性：给切面提供一个唯一标志
    - ref属性：指定通知类(Logger)bean的id
- 4 在aop:aspect标签的内部使用对应的标签来配置通知的类型:前置通知,后置通知,异常通知,最终通知,环绕通知。 
    - 例如<aop:before> ...
    - method属性用于指定通知类中哪一个方法是（前置）通知
- 5 <aop:before>中的pointcut属性：用于指定切入点表达式，该表达式的含义是指要对业务层中的哪些方法增强
    - 切入点表达式的写法：
        - 关键字:execution(表达式)
        - 表达式：访问修饰符 + 返回值 + 包名.类名.方法名（参数列表）
        - 标准的表达式写法：`public void com.study.service.impl.AccountServiceImpl.saveAccount()`

## 切入点表达式的写法

- 访问修饰符可以省略
- 返回值可以使用通配符`*` ，表示任意返回值
- 包名可以使用通配符表示任意包，但是有几级包就需要写几个`*.`
    - 例如`execution( * *.*.*.*.AccountServiceImpl.saveAccount())`
- 包名可以使用`..`表示当前包及其子包
    - 例如通配一级包下任意同名方法`execution( * *..AccountServiceImpl.saveAccount())`
- 类名和方法名都可以使用`*`来实现通配
    - `execution( * *..*.*())`增强所有无参数方法
- 参数列表可以直接写数据类型：基本类型写名称、引用类型写`包名.类名`的方式
    - 例如`java.lang.String`
- 参数类型可以使用通配符`*`表示任意类型，但是必须有参数
- 可以使用`..`表示有无参数均可，有参数可以是任意类型、任意个数
- 全通配方法：`* *..*.*(..)`
- **实际开发中切入点表达式的通常写法**：切到业务层实现类下的所有方法
    - 例如:`execution(* com.study.service.impl.*.*(..))`
    
## Advice的类型
- ![wyU59UB](https://i.imgur.com/wyU59UB.jpg)
- **前置通知：`<aop:before>`，在切入点方法（业务方法）执行之前执行**
- **后置通知：`<aop:after-returning>`，在切入点方法（业务方法）正常执行之后执行**
    - ![MDWuwhQ](https://i.imgur.com/MDWuwhQ.png)

- **异常通知：`<aop:after-throwing>`，，在切入点方法（业务方法）执行产生异常之后执行**
    - **后置通知和异常通知两者永远只能执行一个**
    - ![H3RCGV2](https://i.imgur.com/H3RCGV2.png)
- **最终通知：`<aop:after>`，无论切入点方法（业务方法）是否正确执行，最终通知都会最终执行**

### 环绕通知：`<aop:around>` 【手动控制增强方法何时执行】
  - **Spring中的环绕通知是Spring框架为我们提供的一种可以在代码中手动控制增强方法何时执行的通知**， 使用环绕通知，我们可以用类似自行实现代理模式的方法来增强切入点方法，并在切入点方法被调用的前/后时间点进行增强。
  ![mNmOyO](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/mNmOyO.png)
  - **当配置了环绕通知之后，切入点方法没有执行，而环绕通知方法却执行了** ：通过对比动态代理中的环绕通知代码可以发现，动态代理中的环绕通知有明确的业务层（切入点）方法调用，而我们的代码中没有，所以切入点方法才会没有被调用
  - 解决：Spring框架为我们提供了一个接口：ProceedingJoinPoint, 该接口有一个方法proceed()，此方法就相当于明确调用切入点方法，该接口可以作为环绕通知的方法参数，在程序执行时，Spring框架会为我们提供该接口的实现类供我们使用
  - 环绕通知的写法例子：
      ```java
      // aroundPrintLog是一个环绕通知方法，具有接口参数ProceedingJoinPoint
      public Object aroundPrintLog(ProceedingJoinPoint pjp){
          Object rtValue = null;
          try {
              Object[] args = pjp.getArgs(); // 得到方法执行所需要的参数
              System.out.println("Logger类中的aroundPrintLog方法(前置)");
              rtValue = pjp.proceed(args); // 明确调用业务层（切入点方法）方法
              System.out.println("Logger类中的aroundPrintLog方法（后置）");
              return rtValue;
          } catch (Throwable throwable) {
              System.out.println("Logger类中的aroundPrintLog方法（异常）");
              throw new RuntimeException(throwable);
          }finally {
              System.out.println("Logger类中的aroundPrintLog方法（最终）");
          }
      }
      ```
  - ![bQJSa4N](https://i.imgur.com/bQJSa4N.png)

## 切入点表达式的引用
- 切面内切入点表达式的引用：`<aop:pointcut id="accountServiceImplPt" expression="execution(* com.study.service.impl.*.*(..))"/>`
    - 此标签是写在`<aop:aspect>`标签内部，只能被当前aspect标签使用
    - 配置切入点表达式，id属性用于指定表达式的唯一标志，expression指定表达式内容
- 切面外所有切面可用的引用：提出至外面 `<aop:pointcut id="accountServiceImplPt" expression="execution(* com.study.service.impl.*.*(..))"/>`
    - **该标签必须出现在<aop:config>内部所有的<aop:aspect>标签之前**！
    - 此标签是写在<aop:config>标签内部, 能被整个aop config内配置的所有aspect引用
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    <!--配置spring的IoC，把service对象配置进来-->
    <bean id="accountService" class="com.study.service.impl.AccountServiceImpl"></bean>


    <!--配置Spring IoC管理Logger-->
    <bean id="logger" class="com.study.utils.Logger"></bean>
    <!--开始配置AOP-->
    <aop:config>
        <!--被各个aspect引用的pointcut必须放在所有的aspect之前-->
        <aop:pointcut id="accountServiceImplPt" expression="execution(* com.study.service.impl.*.*(..))"/>
        <!--配置切面-->
        <aop:aspect id="logAdvice" ref="logger">
<!--            &lt;!&ndash;配置前置通知&ndash;&gt;-->
            <aop:before method="beforePrintLog" pointcut="execution(* com.study.service.impl.*.*(..))"></aop:before>
<!--            &lt;!&ndash;配置后置通知&ndash;&gt;-->
            <aop:after-returning method="afterReturningPrintLog" pointcut-ref="accountServiceImplPt"></aop:after-returning>
<!--            &lt;!&ndash;配置异常通知&ndash;&gt;-->
            <aop:after-throwing method="afterThrowingPrintLog" pointcut-ref="accountServiceImplPt" ></aop:after-throwing>
<!--            &lt;!&ndash;配置最终通知&ndash;&gt;-->
            <aop:after method="afterAllPrintLog" pointcut-ref="accountServiceImplPt"></aop:after>
        <!--配置环绕通知-->
<!--        <aop:around method="aroundPrintLog" pointcut-ref="accountServiceImplPt"></aop:around>-->


        </aop:aspect>
    </aop:config>

</beans>

```
















