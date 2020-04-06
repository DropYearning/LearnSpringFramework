package com.study.test;

import com.study.domain.Account;
import com.study.service.IAccountService;
import config.SpringConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 使用junit单元测试
 * 配置spring整合Junit
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {


    // 需要替换成spring提供的main方法之后才能使用
    @Autowired
    private IAccountService as = null;


    @Test
    public void testFindAll() {
        // 3 执行方法
        List<Account> accounts = as.findAllAccount();
        for (Account account : accounts){
            System.out.println(account);
        }
    }

    @Test
    public void testFindOne() {
        // 3 执行方法
        Account account = as.findAccountById(1);
        System.out.println(account);
    }

    @Test
    public void testSave() {
        Account account = new Account();
        account.setName("testSave");
        account.setMoney(123456);
        // 3 执行方法
        as.saveAccount(account);
    }

    @Test
    public void testUpdate() {
        // 3 执行方法
        Account account = as.findAccountById(5);
        account.setName("testUpdate");
        as.updateAccount(account);
    }

    @Test
    public void testDelete() {
        // 3 执行方法
        as.deleteAccount(4);
    }
}
