package com.study.test;

import com.study.domain.Account;
import com.study.service.IAccountService;
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
@ContextConfiguration(locations = "classpath:bean.xml")
public class AccountServiceTest {


    // 需要替换成spring提供的main方法之后才能使用
    @Autowired
    private IAccountService as = null;

    @Test
    public void testTransfer() {
        as.transfer("aaa", "bbb", 5f);
    }
}
