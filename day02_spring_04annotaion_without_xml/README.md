# LearnSpringFramework-day2-Spring-基于注解IoC(不写Bean.XML)的CRUD
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## 注解方式的分析
- 使用纯注解并没有更省事，结合XML和注解两种方式一起使用比较常见
- 使用哪种更方便就使用哪一种


## 脱离bean.XML时要用到的注解
- 创建一个配置类config.SpringConfig来替代bean.xml
- **@Configuration** : 指定当前类是一个Spring的配置类
    - 细节：当配置类作为AnnotationConfigApplicationContext对象创建的参数时，该注解可以省略
        - `ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);`
        - 注意后面的参数`SpringConfig.class`
    
- **@ComponentScan** : 用于通过注解指定Spring创建容器时要扫描的包
    - 属性 value(="basePackages"")： 用于指定创建容器时要扫描的包（使用类路径书写）
    - `@ComponentScan(basePackages = "com.study")`
    - 上面一条等同于：`<context:component-scan base-package="com.study"></context:component-scan>`
    - **@ComponentScans** ：支持配置多个要扫描的包
- **@Bean** : 用于把当前方法的返回值作为Bean对象存入Spring的IoC容器中。
    - 属性name : 用于指定bean的id。**不写时默认值是当前方法的名称**。
    - ![FJtIBDP](https://i.imgur.com/FJtIBDP.png)
    - 细节：使用注解配置方法时，如果方法有参数，Spring方法会去容器中查找有没有可用的Bean对象。查找的方式和 **@Autowired** 注解的方式是一样的。
- **@Import** : 用于导入其他的配置类
    - 参数直接使用要导入的类的字节码即可
    - `@Import(JdbcConfig.class)`
    - 有Import注解的类也叫主配置类或者父配置类，导入的都是子配置类
    - 也可以是用**并列关系**创建容器，在创建AnnotationConfigApplicationContext的参数里写多个类的.class字节码即可：`ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class, JdbcConfig.class);`
- **@PropertySource** : 用于指定properties文件的位置，从而实现从properties文件中读取参数，进一步解藕，读出的参数可以使用SpEL表达式获取
    - 属性value:指定文件的名称和路径，关键字`classpath:`表示是在类路径下

- **使用注解时要使用ApplicationContext的子类AnnotationConfigApplicationContext获取核心容器**
    - `ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);`
    - 上面一条等同于以前的：`ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");`

- 注意：在.java程序中书写jdbc URL时不需要使用`&amp`;来转义`&`；在.xml中中书写jdbc URL时需要使用`&amp;`来转义`&`；在.properties文件中书写时也不需要使用`&amp`;来转义`&`

## Spring中使用注解配置的xml头
告知Spring在创建容器时要扫描的包！配置所需要的标签不是在beans这个约束中，而是一个名称为context名称空间和约束中

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">
```

`<context:component-scan base-package="com.study"></context:component-scan>`
扫描该包和该包下的所有子包上的注解

## 用于创建对象的注解

- 它们的作用就和XML文件中编写一个<bean>标签实现的功能是一样的
- **@Component**：用于把当前类对象存入Spring核心容器中
    - 属性**value**：用于指定bean的id。不写时**默认值是当前类名且首字母改小写**。
- 下面三个注解的作用与属性与@Component一模一样。它们三个是Spring框架为我们提供明确的三层使用的注解，使我们的三层架构更加清晰
    - **@Controller**：一般用在表现层
    - **@Service**：一般用在业务层
    - **@Repository**：一般用在持久层

## 用于注入数据的注解

- 它们的作用就和XML文件中的<bean>标签中的<property>的作用是一样的

- **@Autowired**：自动按照类型注入，只要容器中有唯一的一个bean对象类型和要注入的变量类型匹配，就可以注入成功

    - 出现位置：可以是变量成员，也可以是方法成员
    - 如果IoC容器中没有任何bean类型和要注入的对象类型匹配的话，则报错
    - 如果存在同名的bean类型与要注入的类型匹配的话
        - 如果有多个匹配时，首先按类型找出所有匹配的对象
        - 使用变量名称作为bean的id在类型符合的对象中查找，若有一致的则注入成功
        - 如果没有一致的则报错，`No qualifying bean of type 'com.study.dao.IAccountDao' available: expected single matching bean but found 2: accountDao1,accountDao2`                         

- 在使用注解注入时，set方法就不是必须的了

    - ![wXdf7ax](https://i.imgur.com/wXdf7ax.png)

- **@Qualifier**:在按照类型注入的基础之上再按照名称注入。它在给类成员注入时不能单独使用，但是在给方法参数注入时可以

    - 属性**value**：用于指定bean的id
    - **在给类成员注入时不能独立使用，必须和@Autowired配合**

- **@Resource**：直接按照bean的id注入，可以独立使用，不依托@Autowired

    - 属性**name**：用于指定bean的id

    - @Resource是JDK带的注解，需要配置maven导入javax.annotation

        ```
           <dependency>
               <groupId>javax.annotation</groupId>
               <artifactId>javax.annotation-api</artifactId>
               <version>1.3.1</version>
           </dependency>
        ```

- **@Autowired、@Qualifier、@Resource只能注入其他Bean类型的数据，而基本类型和String类型无法使用这三个注解实现**

- **集合类型的注入只能通过XML实现**

- **@Value** :用于注入基本类型数据和String类型的数据

    - 属性value：用于指定数据的值，它支持使用Spring中的SpEL（也就是Spring的el表达式）
    - **SpEL表达式**： 使用 # 作为定界符，所有在**大括号**中的字符都将被认为是 SpEL；
    - SpEL写法：`${表达式}`

## 用于改变作用范围的注解

- 它们的作用就和在bean标签中使用scope的作用是一样的
- **@Scope**:用于指定Bean的作用范围
    - 属性value：指定范围的取值【singleton(default)/prototype】

## 和生命周期相关的注解(了解)

- 它们的作用就和在bean标签中使用init-method和destroy-method是一样的
- **@PreDestroy** :指定销毁方法（在Destroy前执行）
- **@PostConstruct** :指定初始化方法（在构造函数之后执行）
- 注意`ApplicationContext`作为父类型没有close方法，要使用`ClassPathXmlApplicationContext`接收核心容器对象
- `ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");`
- 注意多例对象和单例对象的销毁。单例对象的销毁才由Spring核心容器负责，多例对象的销毁由GC负责


