# LearnSpringFramework-day2-Spring-整合junit测试
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## Spring整合Junit的问题分析
之前的测试方法写法：
```java
public class AccountServiceTest {
    private ApplicationContext ac;
    private IAccountService as;

    @Before
    public void init(){
        // 1 获取容器
        ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        // 2 得到业务层对象
        as = ac.getBean("accountService", IAccountService.class);
    }

    @After
    public void destroy(){

    }

    @Test
    public void testFindAll() {
        // 3 执行方法
        List<Account> accounts = as.findAllAccount();
        for (Account account : accounts){
            System.out.println(account);
        }
    }
}
```

- 尽管可以在Test类中使用@Before和@After等标签实现测试时的初始化和销毁工作，但是这些内容不应该被测试工程师所关心
- junit单元测试中，没有main方法也能执行，因为junit集成了一个main方法，该方法就会判断当前测试类中哪些方法有 @Test注解，若有junit就让有@Test注解的方法执行
- junit不会管我们是否采用spring框架。在执行测试方法时，junit根本不知道我们是不是使用了spring框架，所以也就不会为我们读取配置文件/配置类创建spring核心容器
- 由以上三点可知：当执行Junit测试方法执行时，Junit是不知道IoC容器的存在的，就算在测试类中写了 **@Autowired** 注解，也无法实现注入【因为@Autowired是Spring中的注解】


## 解决：导入Spring整合junit的jar包（Maven坐标）

```xml
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
```

- 修改junit内置的不能加载Spring容器的main方法，使之可以在运行时创建Spring核心容器
- 使用junit提供的一个注解把junit内置的main方法替换，替换成spring提供的main方法
    - 在测试类前加上`@RunWith(SpringJUnit4ClassRunner.class)`注解
    - 需要告知Spring的Runner创建IoC容器时是基于XML还是注解，并且说明相应位置
        - **@ContextConfiguration** : 
            - 【使用XML配置】属性 locations :指定XML文件的位置，并且前面要加上`classpath:`的关键字表示在类路径下 `@ContextConfiguration(locations= {"classpath:bean.xml"})`
            - 【使用注解配置】属性classes：指定注解类所在的位置（字节码）`@ContextConfiguration(classes = SpringConfig.class)`
- **接下来就可以在junit测试类的成员中使用 @Autowired注解**
- 当使用Spring 5.x版本需要整合junit时，需要junit的jar包版本高于4.12及以上

## 前后对比
修改前：
```
public class AccountServiceTest {
    private ApplicationContext ac;
    private IAccountService as;

    @Before
    public void init(){
        // 1 获取容器
        ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        // 2 得到业务层对象
        as = ac.getBean("accountService", IAccountService.class);
    }

    @Test
    public void testFindAll() {
        // 3 执行方法
        List<Account> accounts = as.findAllAccount();
        for (Account account : accounts){
            System.out.println(account);
        }
    }
}


```

修改后：
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {

    // 需要替换成spring提供的main方法之后才能使用
    @Autowired
    private IAccountService as = null;

    @Test
    public void testFindAll() {
        // 3 执行方法
        List<Account> accounts = as.findAllAccount();
        for (Account account : accounts){
            System.out.println(account);
        }
    }

}
```
