package com.study.ui;

import com.study.factory.BeanFactory;
import com.study.service.IAccountService;

/**
 * 模拟客户端（表现层）：用户调用业务
 */
public class client {
    public static void main(String[] args) {
        //IAccountService as = new AccountServiceImpl();
        for (int i = 0; i < 5; i++) { // 测试单例效果
            IAccountService as = (IAccountService) BeanFactory.getBean("accountServiceImpl");
            System.out.println(as);
            as.saveAccount();
        }

    }
}
