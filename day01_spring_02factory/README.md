# LearnSpringFramework-day1-Spring-通过工厂解藕
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## Bean
- Bean:在计算机英语中有可重用组件的含义
- JavaBean: JavaBean ≠ 实体类， 用Java语言便携的可重用组件

## 使用工厂模式解藕
- Bean工厂就是用来创建service和dao对象的。
    - 1、需要一个配置文件来配置service和dao。配置的内容：  唯一标志 = 全限定类名（key-value的形式）
    - 2、通过读取配置文件中的配置内容，反射创建对象
- 配置文件可以是XML也可以是properties

## 仍然存在的问题分析（单例改进）
- ![vCiyLbS](https://i.imgur.com/vCiyLbS.png)
- 改进：创建一个Map容器保存生产出的Bean对象

## NPE提示
- AccountServiceImpl类中的accountDao若在函数外作为类的成员变量初始化会报NPE
- ![qg08Lct](https://i.imgur.com/qg08Lct.png)
- 解决方法：将accountDao从类的成员变量移到方法内部
- 可能原因：先创建了ServiceImpl对象，但是ServiceImpl需要用到DaoImpl对象，而DaoImpl为空还没有被创建






