package com.study.test;

import com.study.domain.Account;
import com.study.service.IAccountService;
import config.SpringConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * 使用junit单元测试xml配置spring
 */
public class AccountServiceTest {
    private ApplicationContext ac;
    private IAccountService as;


    @Before
    public void init(){
        // 1 获取容器
        ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        // 2 得到业务层对象
        as = ac.getBean("accountService", IAccountService.class);
    }

    @After
    public void destroy(){

    }


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
