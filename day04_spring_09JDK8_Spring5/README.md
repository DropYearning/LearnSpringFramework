# LearnSpringFramework-day4-Spring

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## Spring5新特性
- jdk8在创建对象的效率上：
    - ![SnNHhM5](https://i.imgur.com/SnNHhM5.png)
- Spring5基于JDK8编写
- Spring5要求Tomcat8.5版本以上
- Spring Framework 5.0 现在支持候选组件索引作为类路径扫描的替代方案。该功能已经在类路径扫描器中添加，以简化添加候选组件标识的步骤。**从索引读取实体而不是扫描类路径对于小于200个类的小型项目是没有明显差异**。但对大型项目影响较大。加载组件索引开销更低。因此，随着类数的增加，索引读取的启动时间将保持不变。
- 此次 Spring 发行版本的一个激动人心的特性就是新的**响应式**堆栈 WEB 框架。这个堆栈完全的响应式且**非阻塞**，适合于事件循环风格的处理，可以进行少量线程的扩展。