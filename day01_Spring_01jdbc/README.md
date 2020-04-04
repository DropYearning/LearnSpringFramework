# LearnSpringFramework-day1-Spring-JDBC
  
  学习Spring框架
  
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)
  
## 程序的耦合
- 程序的耦合:程序间的依赖关系
    - 包括：类之间的依赖，方法之间的依赖
- 解藕：降低程序间的依赖关系
- 实际开发时：应该做到编译时不依赖，运行时才依赖。
- 解藕的思路：
    - 1、使用反射来创建对象，而避免使用new创建对象。前者依赖的是forName中的参数字符串，后者依赖的是具体的类
    - 2、通过读取配置文件来出获取要创建的全限定类名