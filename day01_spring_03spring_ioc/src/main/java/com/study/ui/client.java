package com.study.ui;

import com.study.dao.IAccountDao;
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
        IAccountDao aDao = ac.getBean("accountDao", IAccountDao.class);
            // 后面的IAccountDao.class用于告诉ApplicationContext将获取到的对象转换为 IAccountDao.class
            // 也可以省去第二个参数以IAccountService的形式书写
        System.out.println(as);
        System.out.println(aDao);
        as.saveAccount();


        //// ---------BeanFactory(延迟加载)----------
        //Resource resource = new ClassPathResource("bean.xml");
        //BeanFactory beanFactory = new XmlBeanFactory(resource);
        //IAccountService as = (IAccountService) beanFactory.getBean("accountService");
        //System.out.println(as); // 延迟加载会在用到该对象时才会调用构造函数创建对象



    }
}
