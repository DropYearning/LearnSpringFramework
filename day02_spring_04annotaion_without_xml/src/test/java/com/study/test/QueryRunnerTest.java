package com.study.test;

import com.study.service.IAccountService;
import config.SpringConfig;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试QueryRunner是否是单例的
 */
public class QueryRunnerTest {
    @Test
    public void testQueryRunner() {
        // 1 获取容器
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        // 2 得到QueryRunner对象
        QueryRunner runner = ac.getBean("runner", QueryRunner.class);
        QueryRunner runner1 = ac.getBean("runner", QueryRunner.class);
        System.out.println(runner == runner1);
    }
}
