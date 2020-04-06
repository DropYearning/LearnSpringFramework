package com.study.service.impl;

import com.study.dao.IAccountDao;
import com.study.dao.impl.AccountDaoImpl;
import com.study.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * 账户的业务层实现类
 *  XML配置的方法：
 *  <bean id="accountService" class="com.study.service.impl.AccountServiceImpl"
 *        scope="" init-method="" destroy-method +"">
 *        <property name="" value="" | ref =""></property>
 * </bean>
 */

@Component(value = "accountService")
@Scope(value = "singleton")
public class AccountServiceImpl implements IAccountService {

    //@Autowired
    //@Qualifier("accountDao1")
    @Resource(name = "accountDao2")
    private IAccountDao accountDao ;

    @PostConstruct
    public void  init(){
        System.out.println("AccountServiceImpl中的初始化方法执行了");
    }

    @PreDestroy
    public void  destroy(){
        System.out.println("AccountServiceImpl中的销毁方法执行了");
    }


    public AccountServiceImpl(){
        System.out.println("AccountServiceImpl对象已创建");
    }
    public void  saveAccount(){
        accountDao.saveAccount();
    }
}
