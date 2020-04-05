# LearnSpringFramework-day1-Spring-分析Spring管理Bean的细节
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)


## 说明
- 此处删除了dao包，只保留了service包，具体通过serviceBean对象的创建分析细节

## 创建bean的三种方式
### 方式一：使用默认构造函数创建
- 在Spring的配置文件中使用Bean标签配以id和class属性后，且没有其他属性和标签时采用的就是**默认构造函数创建Bean对象**
- 此时如果类中没有默认构造函数，则对象无法创建
- 例如`<bean id="accountService" class="com.study.service.impl.AccountServiceImpl"></bean> `
- 此处如果没有默认构造函数，idea也会在xml中红线报错提示

> 该方法适用于：创建自己写的类的Bean对象，且明确存在默认构造函数

### 方式二：使用普通工厂中的方法创建对象（使用某个类中的方法创建对象）
- 首先在xml中声明工厂类（或者某个具体类）的Bean配置
    - `<bean id="instanceFactory" class="com.study.factory.InstanceFactory"></bean>`
- 然后在xml中通过factory-bean和factory-method参数来调用工厂中的创建方法
    - `<bean id="accountService" factory-bean="instanceFactory" factory-method="getAccountService"></bean>`

> 适用于调用别的jar包中的某个方法来创建对象 / 或者类中存在非构造函数的实例化方法，例如getInstanceofXXX()方法

### 方式三：使用静态工厂中的静态方法创建对象（使用某个类中的静态方法创建对象）
- `class="com.study.factory.StaticFactory"`指明工厂类名
- `factory-method="getAccountService"`指明调用的**静态**方法名
- `<bean id="accountService" class="com.study.factory.StaticFactory" factory-method="getAccountService"></bean>`

## bean对象的作用范围
> 使用Spring框架创建出的Bean对象默认就是单例的，使用bean标签的scope属性可以调整

- bean标签的scope属性：用于指定bean的作用范围。可选取值有：
    - singleton:(默认缺省值)单例
    - prototype:多例的
    - request:作用于web应用的请求范围
    - session:作用于web应用的会话范围
    - global-session: 作用于集群环境的全局会话范围，当不是集群环境时，global-session = session
        - ![5b9Lr0L](https://i.imgur.com/5b9Lr0L.png)
    - 常用：singleton和prototype

## bean对象的生命周期
- 单例对象：单例对象的生命周期与容器相同
    - 出生：当Spring容器创建时，对象就被创建
    - 活着：容器存在，单例对象就还在
    - 死亡：容器销毁，单例对象释放
- 多例对象：
    - 出生：当使用到对象时，Spring框架才会创建实例对象
    - 活着：对象只要是在被使用的过程中一直存活
    - 死亡：当对象长时间不用，且没有别的对象引用它时，由Java的垃圾回收器回收。
- 需要在<bean>标签中增加 `init-method` 和 `destory-method` 属性，对应的类中也要有初始化和销毁方法
