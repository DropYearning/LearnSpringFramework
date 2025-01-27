# LearnSpringFramework-day3-Spring-AOP概念以及基于XML的Spring AOP配置

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](
  
## 什么是AOP？
- 在软件业，AOP为A**spect Oriented Programming**的缩写，意为：面向切面编程，通过预编译方式和运行期间动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。
- 作用：在程序运行期间，不修改源码对已有方法进行增强。
- 优势：减少重复代码、提高开发效率、维护方便

## Spring中的AOP
- Spring中可以通过**配置**AOP的方式实现*day03_spring_01account_improve*中将账户业务类与事务操作解耦。
- `Joinpoint(连接点)【可以理解为业务层接口中所有可以拦截的方法】`:所谓连接点是指那些被拦截到的点。**在 spring 中,这些点指的是方法**,因为spring只支持方法类型的连接点。
- `Pointcut(切入点)【可以理解为那些被增强的方法】`: 所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义
    - 所有的切入点都是连接点，但不是所有的连接点都是切入点
- `Advice(通知/增强)`:所谓通知是指拦截到 Joinpoint 之后所要做的事情。
    - 通知是提供了部分增强功能的类提供的方法。原来例子中的TxManager类提供的事务支持方法都叫通知。
    - 通知的类型：
      - 前置通知
      - 后置通知
      - 异常通知
      - 最终通知
      - 环绕通知 
    - ![LUlK8OY](https://i.imgur.com/LUlK8OY.jpg)
- `Introduction(引介)`: 引介是一种特殊的通知在不修改类代码的前提下, Introduction 可以在运行期为类动态地添加一些方法或 Field。
- `Target(目标对象)`:代理的目标对象。
- `Proxy（代理）`: 一个类被 AOP 织入增强后，就产生一个结果代理类。
- `Weaving(织入)`:是指把增强应用到目标对象来创建新的代理对象的过程。spring 采用动态代理织入，而 AspectJ 采用编译期织入和类装载期织入。
- `Aspect(切面)`:是切入点和通知（引介）的结合。

## AOP开发的步骤
- 开发阶段（我们做的）：
    - 编写核心业务代码（开发主线）：大部分程序员来做，要求熟悉业务需求。
    - 把公用代码抽取出来，制作成通知。（**开发阶段最后再做**）：AOP经验丰富的人员来做。
    - 在配置文件中，**声明切入点与通知间的关系，即切面**。：AOP经验丰富的人员来做。
- 运行阶段（Spring 框架完成的）: **Spring 框架监控切入点方法的执行。一旦监控到切入点方法被运行，使用代理机制，动态创建目标对象的代理对象，根据通知类别，在代理对象的对应位置，将通知对应的功能织入，完成完整的代码逻辑运行。**

## Spring AOP的配置实例
### 编写业务类和通知方法
- 有一个业务层实现类AccountServiceImpl，和一个日志工具类Logger，需求是在切入点方法执行之前执行（切入点方法就是业务层方法）Logger中的printLog方法
```java
/**
 * 账户的业务层接口
 */
public interface IAccountService {
    void saveAccount(); // 模拟保存账户（无返回值，无参数）

    void updateAccount(int i); // 模拟更新某个账户（无返回值，有参数）

    int deleteAccount();// 删除账户（有返回值，无参数
}

/**
 * 账户的业务层实现类
 */
public class AccountServiceImpl implements IAccountService {
    @Override
    public void saveAccount() {
        System.out.println("执行了AccountServiceImpl保存方法");
    }

    @Override
    public void updateAccount(int i) {
        System.out.println("执行了AccountServiceImpl更新方法 " + i);
    }

    @Override
    public int deleteAccount() {
        System.out.println("执行了AccountServiceImpl删除方法");
        return 0;
    }
}
```
Logger类：
```java
/**
 * 用于记录日志的工具类，提供了公共的代码
 */
public class Logger {
    // 用于在控制台打印日志，计划让其在切入点方法执行之前执行（切入点方法就是业务层方法）
    public void printLog(){
        System.out.println("Logger类中的printLog方法开始记录日志");
    }
}
```

### 增加AOP的Maven依赖
```xml
        <!--解析切入点表达式用-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.7</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
```

### Spring AOP的XML头
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
</beans>
```

### Spring中基于XML的AOP配置步骤
- 1 把通知的Bean(Logger)也交给Spring管理
- 2 使用`<aop:config>`标签表明开始AOP的配置
- 3 使用`<aop:aspect>`标签表明开始配置切面
    - id属性：给切面提供一个唯一标志
    - ref属性：指定通知类(Logger)bean的id
- 4 在`<aop:aspect>`标签的内部使用对应的标签来配置通知的类型:前置通知,后置通知,异常通知,最终通知,环绕通知。 
    - <aop:aspect>：使用aspectJ方式实现AOP需要的标签。
    - <aop:before /> ：为指定的切点绑定前置通知，该通知会先于切点方法执行。
    - <aop:after />：为指定的切点绑定后置通知，会后于切点方法执行。
    - <aop:after-returning />：为指定的切点绑定后置通知，会后于切点方法执行。
    - <aop:throwing /> ：为指定的切点方法绑定异常通知，当切点方法执行时出现异常，就会执行该通知。
    - <aop:around />：为指定的切点方法绑定环绕通知。
    - method属性用于指定通知类中哪一个方法是（前置）通知
- 5 配置`pointcut`切入点表达式：用于指定切入点表达式，该表达式的含义是指要对业务层中的哪些方法增强
    - 切入点表达式的写法：`execution(表达式)`
        - 表达式：访问修饰符 + 返回值 + 包名.类名.方法名（参数列表）
        - 标准的表达式写法：`<aop:before method="printLog" pointcut=""execution(public void com.study.service.impl.AccountServiceImpl.saveAccount())></aop:before>`
        - 简略的表达式写法：`execution(* com.study.service.impl.*.*(..))` 【匹配com.study.service.impl包下的所有类的所有方法】

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    ... ...
    <!--配置spring的IoC，把service对象配置进来-->
    <bean id="accountService" class="com.study.service.impl.AccountServiceImpl"></bean>

    <!--配置Spring IoC管理Logger-->
    <bean id="logger" class="com.study.utils.Logger"></bean>
    <!--开始配置AOP-->
    <aop:config>
        <!--配置切面-->
        <aop:aspect id="logAdvice" ref="logger">
            <!--配置通知的类型并且建立通知方法和切入点方法的关联：printLog是前置通知-->
            <!-- public void com.study.service.impl.AccountServiceImpl.saveAccount() -->
            <!--**实际开发中切入点表达式的通常写法**：关联到到业务层实现类下的所有方法-->
            <aop:before method="printLog" pointcut="execution(* com.study.service.impl.*.*(..))"></aop:before>
        </aop:aspect>

    </aop:config>
</beans>
```

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
  - `pointcut="execution(* com.study.service.impl.*.*(int))`
  - **参数类型可以使用通配符`*`表示任意类型，但是必须有参数**
  - 可以使用`..`表示有无参数均可，有参数可以是任意类型、任意个数: `pointcut="execution(* com.study.service.impl.*.*(..))`
- 全通配方法：`* *..*.*(..)`
- **实际开发中切入点表达式的通常写法**：切到业务层实现类下的所有方法
    - 例如:`execution(* com.study.service.impl.*.*(..))`
















