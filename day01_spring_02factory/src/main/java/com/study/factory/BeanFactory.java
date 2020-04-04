package com.study.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 是一个创建Bean对象的工厂
 *  Bean:在计算机英语中有可重用组件的含义
 *  JavaBean: JavaBean ≠ 实体类， 用Java语言便携的可重用组件
 *
 *  Bean工厂就是用来创建service和dao对象的。
 *  1、需要一个配置文件来配置service和dao
 *      配置的内容：  唯一标志 = 全限定类名（key-value的形式）
 *  2、通过读取配置文件中的配置内容，反射创建对象
 */
public class BeanFactory {
    private static Properties props; //定义一个Properties对象
    private static Map<String, Object> beans;// 创建一个Map容器保存生产出的Bean对象


    static {  // 使用静态代码块为Properties对象赋值，静态代码块会在类加载时调用
        try {
            props = new Properties();
            InputStream in = BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties");
            // 获取props文件流对象
            // 使用类加载器来获取流对象比较好
            props.load(in);
            // 实例化容器
            beans = new HashMap<String, Object>();
            // 取出配置文件中所有的key
            Enumeration keys = props.keys();
            // 遍历枚举
            while (keys.hasMoreElements()){
                // 取出每一个key
                String key = keys.nextElement().toString();
                System.out.println(key);
                // 根据key获取value
                String beanPath = props.getProperty(key);
                System.out.println(beanPath);
                // 反射创建对象
                Object value = Class.forName(beanPath).newInstance();
                System.out.println(value);
                // 存入Map中
                beans.put(key, value);

            }
            System.out.println(beans);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("初始化读取配置文件错误！");
        }
    }


    /**
     * 修改后：根据Bean的名称获取Bean对象
     * (根据Bean的名称直接获取对象)
     */
    public static Object getBean(String beanName){
        return beans.get(beanName);
    }


    ///**
    // * 根据Bean的名称获取Bean对象
    // */
    //public static Object getBean(String beanName){
    //    Object bean = null;
    //    try {
    //        String beanPath = props.getProperty(beanName);
    //        bean = Class.forName(beanPath).newInstance();
    //
    //    }catch (Exception e){
    //        e.printStackTrace();
    //    }
    //    return bean;
    //}
}
