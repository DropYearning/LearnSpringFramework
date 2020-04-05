package com.study.service.impl;

import com.study.service.IAccountService;

import java.util.Date;

/**
 * 演示注入方式二：使用set方法注入
 */
public class AccountServiceImpl2 implements IAccountService {

    // 如果是经常变化的数据，并不适用于依赖注入的关系，此处只是作为演示
    private String name; // 需要注入的字符串类型
    private Integer age; // 需要注入的基本类型的包装类
    private Date birthday; // 需要注入的其他类

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public void  saveAccount(){
        System.out.println("AccountServiceImpl2中的saveAccount方法执行了：" + name +","+ age + ","  + birthday);
    }

}
