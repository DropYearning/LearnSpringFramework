package com.study.utils;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 用于记录日志的工具类，提供了公共的代码
 */
public class Logger {

    // 前置通知，计划让其在切入点方法执行之前执行（切入点方法就是业务层方法）
    public void beforePrintLog(){
        System.out.println("前置通知：Logger类中的前置通知");
    }

    // 后置通知
    public void afterReturningPrintLog(){
        System.out.println("后置通知：Logger类中的后置通知");
    }

    // 异常通知
    public void afterThrowingPrintLog(){
        System.out.println("异常通知：Logger类中的异常通知");
    }

    // 最终通知
    public void afterAllPrintLog(){
        System.out.println("最终通知：Logger类中的最终通知");
    }

    // 环绕通知
    public Object aroundPrintLog(ProceedingJoinPoint pjp){
        Object rtValue = null;
        try {
            Object[] args = pjp.getArgs(); // 得到方法执行所需要的参数
            System.out.println("Logger类中的aroundPrintLog方法(前置)");
            rtValue = pjp.proceed(args); // 明确调用业务层（切入点方法）方法
            System.out.println("Logger类中的aroundPrintLog方法（后置）");
            return rtValue;
        } catch (Throwable throwable) {
            System.out.println("Logger类中的aroundPrintLog方法（异常）");
            throw new RuntimeException(throwable);
        }finally {
            System.out.println("Logger类中的aroundPrintLog方法（最终）");
        }
    }

}
