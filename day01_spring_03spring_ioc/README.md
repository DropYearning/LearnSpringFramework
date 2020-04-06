# LearnSpringFramework-day1-Spring-通过Spring框架创建Bean
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## Spring中xml文件的头（使用<beans>标签配置）

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">
```



## 把对象创建交给Spring

- 暂时使用bean.xml配置需要Spring创建的Bean对象

## Spring中ApplicationContext的3个常用实现类
![9Ehm5om](https://i.imgur.com/9Ehm5om.png)
- Spring中**ApplicationContext的3个常用实现类它在构建核心容器时，创建对象采取的策略是使用立即加载**。
- 也就是说，**只要一读取完配置文件马上就创建配置文件中的所有对象**。

### ClassPathXmlApplicationContext
- 可以加载类路径下的配置文件，**要求配置文件必须在类路径下**
- 兼容性比FileSystemXmlApplicationContext好，实际开发中更常用

### FileSystemXmlApplicationContext
- 可以加载磁盘任意路径下的配置文件，必须要有文件的访问权限

### AnnotationConfigApplicationContext
- 用于读取注解创建容器（day02的内容）

## BeanFactory
- **BeanFactory在构建核心容器时创建对象采取的策略是延迟加载。**
- 也就是说，什么时候根据id获取对象了，什么时候才真正创建对象

## Bean对象立即加载和延迟加载的选择
- 使用单例对象（模式）时，使用ApplicationContext创建较好
- 使用多例对象时使用BeanFactory创建较好
- BeanFactory接口是比ApplicationContext更高层的接口，实际开发中更多地采用ApplicationContext接口来定义容器对象
