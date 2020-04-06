# LearnSpringFramework-day3-Spring-动态代理
  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)

## 代理模式
- ![trtQMMi](https://i.imgur.com/trtQMMi.png)
- **代理模式**是一种**结构型设计模式**， 让你能够提供对象的替代品或其占位符。 代理控制着对于原对象的访问， 并允许在将请求提交给对象前后进行一些处理。


## 动态代理
- 动态代理的特点：字节码随用随创建，随用随加载
- 动态代理的作用：不修改源码的作用下对方法增强
- 动态代理的分类：
    - 基于接口的动态代理
        - 涉及的类：Proxy
        - 提供者：JDK
    - 基于子类的动态代理
    
> 动态代理和装饰着模式的区别是：装饰者模式一开始必须有一个类，而动态代理是随着使用过程创建和加载

## 基于接口的动态代理
- 使用JDK(java.lang.reflection) Proxy类中的newProxyInstance方法
- `public static Object newProxyInstance​(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)`
    - ClassLoader:类加载器，它是用于加载代理对象的字节码的，**填的被代理对象的类加载器**
    - Class[]:字节码数组，用于让代理对象和被代理对象有相同的方法，**要代理谁就填上谁的类.getClass().getInterfaces()**
    - InvocationHandler: 用于提供**增强**的代码。**一般都是填入一个该接口的实现类，通常使用匿名内部类。**此接口的实现类都是谁用谁来写
    - InvocationHandler接口中只有一个方法`invoke`：执行被代理对象的任何接口方法都会经过该方法（拦截功能）
        - invoke方法中含有下面三个参数：
        - proxy:代理对象的引用
        - method：当前执行的方法
        - args：当前执行方法所用的参数
        - 返回值: 和被代理对象有相同的返回值
        - 最简单的代理：
            ```
           @Override
                      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          return  method.invoke(producer, args);
          }
            ```
- 该方法的返回值是Object，需要强转到需要的类型

电脑代理商卖电脑的例子：
```java
final Producer producer = new Producer(); // 匿名内部类访问外部变量，需要其是final的
// 创建一个代理对象proxyProducer
IProducer proxyProducer = (IProducer)Proxy.newProxyInstance(producer.getClass().getClassLoader(), producer.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //return  method.invoke(producer, args); // 最简单的代理——什么都不增强，纯粹直接调用被代理类的方法

                Object returnValue = null; // 返回值
                // 提供增强的代码（比如在这里由经销商proxy提取20%的佣金）
                // 1 获取方法执行的参数
                Float money = (Float) args[0];
                // 2 判断当前方法是不是销售方法
                if ("saleProduct".equals(method.getName())){
                    returnValue = method.invoke(producer, money*0.8f); // 经销商(proxy)再调用被代理的方法将剩余的销售额转给工厂
                }
                return  returnValue;
            }
        });
        proxyProducer.saleProduct(10000f); // 经销商(proxy)在卖电脑
```

> 若要代理的类没有实现任何接口时，基于接口的动态代理是无法使用的
    
## 基于子类的动态代理



