package com.study.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring的配置类，相当于bean.xml
 */
@Configuration
@ComponentScan("com.study")
@Import({JdbcConfig.class, TransactionConfig.class})
@PropertySource(value = "jdbcConfig.properties")
@EnableTransactionManagement

public class SpringConfig {
}
