package com.study.test;

import com.study.domain.Account;
import com.study.service.IAccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * 使用junit单元测试xml配置spring
 */
public class AccountServiceTest {

    @Test
    public void testFindAll() {
        // 1 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 2 得到业务层对象
        IAccountService as = ac.getBean("accountService", IAccountService.class);
        // 3 执行方法
        List<Account> accounts = as.findAllAccount();
        for (Account account : accounts){
            System.out.println(account);
        }
    }

    @Test
    public void testFindOne() {
        // 1 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 2 得到业务层对象
        IAccountService as = ac.getBean("accountService", IAccountService.class);
        // 3 执行方法
        Account account = as.findAccountById(1);
        System.out.println(account);
    }

    @Test
    public void testSave() {
        Account account = new Account();
        account.setName("testSave");
        account.setMoney(123456);
        // 1 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 2 得到业务层对象
        IAccountService as = ac.getBean("accountService", IAccountService.class);
        // 3 执行方法
        as.saveAccount(account);
    }

    @Test
    public void testUpdate() {
        // 1 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 2 得到业务层对象
        IAccountService as = ac.getBean("accountService", IAccountService.class);
        // 3 执行方法
        Account account = as.findAccountById(4);
        account.setName("testUpdate");
        as.updateAccount(account);
    }

    @Test
    public void testDelete() {
        // 1 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 2 得到业务层对象
        IAccountService as = ac.getBean("accountService", IAccountService.class);
        // 3 执行方法
        as.deleteAccount(4);
    }
}
