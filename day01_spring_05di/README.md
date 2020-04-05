# LearnSpringFramework-day1-Spring-依赖注入DI
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## IoC回顾
- IoC的作用：降低程序间的耦合关系（依赖关系，在当前类中需要用到其他类的对象）
- 依赖只能降低不可能消除
- 改进思路：将依赖关系的管理交由Spring框架来维护，我们只需要在配置文件中说明
    - 我们将依赖关系的维护称之为**依赖注入**

## 依赖注入
- Dependency Injection（DI）:
- 依赖注入能注入的数据有三类：
    - 基本类型和String
    - 其他的Bean类型（在配置文件或者注解中配置过的bean）
    - 复杂类型（集合类型）
## 三种注入的方式：
> 在Spring中的XML中配置注入参数的时候，参数的value虽然都用双引号，看起来是字符串，但是Spring是可以自动处理好从字符串数据到各个需要的数据类型的转换

> 但是除了上述提到的3种能注入的类型外，其他类型填入value中Spring无法自动将双引号的字符串转为对应的数据类型。

### 使用构造函数提供（不常用）
- 借助bean标签中的标签：constructor-arg
- 标签中的属性有：
    - type:指定要注入的数据类型，该数据类型也是构造函数中某个或某些参数的类型
    - index:用于指定要注入的数据在构造函数参数中的位置，从0开始
    - name:用于指定给构造函数中指定名称的参数赋值【常用】
      - 以上三者用于指定给构造函数中哪个参数赋值
    - value: 用于给基本类型和String类型提供值的
    - ref: 引用关联的其他bean类型数据，它指的是在Spring的IoC核心容器中出现过的Bean对象

- 优点：在获取Bean对象时，注入数据是必须的操作，否则对象无法创建成果
- 缺点：改变了Bean对象的实例化方式，使我们在创建对象时如果用不到这些数据，也必须提供。

```
<bean id="accountService" class="com.study.service.impl.AccountServiceImpl" >
        <constructor-arg name="name" value="李白"  ></constructor-arg>
        <constructor-arg name="age" value="18"></constructor-arg>
            <!--注入Date类型时不能只输入一个字符串"1970-01-01"，Spring无法完成类型转换-->
            <!--可以再在下面写一个Date类的bean,并在此通过<ref>id引用-->
        <constructor-arg name="birthday" ref="now"></constructor-arg>
    </bean>
```

### 使用set方法提供（更常用）
- 使用property标签（在bean标签的内部）, 标签的属性有：
    - name:用于指定注入时所调用的set方法名, 名称是set方法名称去掉set，并改首字母为小写：setUsername -> username
    - value: 用于给基本类型和String类型提供值的
    - ref: 引用关联的其他bean类型数据，它指的是在Spring的IoC核心容器中出现过的Bean对象
- 优点：创建对象时没有明确的限制，可以直接使用set方法注入
- 缺点：如果某个成员必须有值，则该方法无法保证一定注入。【比如类中没有写set方法时】

### 使用注解提供

## 复杂类型（数组、集合类型）的注入
- 用于给List集合注入的标签有：list array set
- 用于给Map集合注入的标签有：map, props
- 结构相同，标签可以互换






