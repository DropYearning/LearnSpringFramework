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
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml"); // xml在Resources根目录下,直接写名字即可
        // 2 根据id获取bean对象
        IAccountService as = (IAccountService) ac.getBean("accountService");
        as.saveAccount();

        IAccountService as2 = (IAccountService) ac.getBean("accountService2");
        as2.saveAccount();

        IAccountService as3 = (IAccountService) ac.getBean("accountService3");
        as3.saveAccount();

    }
}
