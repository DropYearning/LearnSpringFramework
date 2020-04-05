package com.study.ui;

import com.study.service.IAccountService;
import com.study.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 模拟客户端（表现层）：用户调用业务
 * 获取Spring容器的IoC核心容器，并根据xml中的id获取对象
 */
public class client {
    public static void main(String[] args) {

        // ---------ApplicationContext(立即加载)----------
        // 1 获取核心容器对象
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml"); // xml在Resources根目录下,直接写名字即可
        // 2 根据id获取bean对象
        IAccountService as = (IAccountService) ac.getBean("accountService");
        //IAccountService as2 = (IAccountService) ac.getBean("accountService");
        //System.out.println(as == as2); // true,构造函数只调用一次
        as.saveAccount();
        ac.close(); // 手动关闭容器，调用bean的销毁方法
            // ApplicationContext父类中没有close方法
            // 多态的注意点：当使用父类的引用指向子类的实例时，只能调用父类中声明的方法


    }
}
