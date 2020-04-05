package com.study.service.impl;

import com.study.service.IAccountService;

import java.util.Date;

/**
 * 演示注入方式一：使用构造函数注入
 */
public class AccountServiceImpl implements IAccountService {

    // 如果是经常变化的数据，并不适用于依赖注入的关系，此处只是作为演示
    private String name; // 需要注入的字符串类型
    private Integer age; // 需要注入的基本类型的包装类
    private Date birthday; // 需要注入的其他类

    public AccountServiceImpl(String name, Integer age, Date birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    @Override
    public void  saveAccount(){
        System.out.println("AccountServiceImpl中的saveAccount方法执行了：" + name +","+ age + ","  + birthday);
    }

}
