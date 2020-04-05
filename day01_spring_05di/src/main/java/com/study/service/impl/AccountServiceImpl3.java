package com.study.service.impl;

import com.study.service.IAccountService;

import java.util.*;

/**
 * 演示复杂类型的注入
 */
public class AccountServiceImpl3 implements IAccountService {

    // 演示下面几种复杂类型的注入
    private String[] myStrs;
    private List<String> myList;
    private Set<String> mySet;
    private Map<String, String> myMap;
    private Properties myProps;

    public void setMyStrs(String[] myStrs) {
        this.myStrs = myStrs;
    }

    public void setMyList(List<String> myList) {
        this.myList = myList;
    }

    public void setMySet(Set<String> mySet) {
        this.mySet = mySet;
    }

    public void setMyMap(Map<String, String> myMap) {
        this.myMap = myMap;
    }

    public void setMyProps(Properties myProps) {
        this.myProps = myProps;
    }

    @Override
    public void  saveAccount(){
        System.out.println("AccountServiceImpl3中的saveAccount方法执行了");
        System.out.println(Arrays.toString(myStrs)); // 打印数组的内容方法
        System.out.println(myList);
        System.out.println(mySet);
        System.out.println(myMap);
        System.out.println(myProps);
    }

}
