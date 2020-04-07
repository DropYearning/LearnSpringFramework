# LearnSpringFramework-day3-使用Spring内置的事务控制接口-纯注解方式

  学习Spring框架
  [Spring教程IDEA版-4天-2018黑马SSM-02_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7vP?from=search&seid=6126662563921252654)
## 纯注解实现Spring内置的事务控制
- **@EnableTransactionManagement** ：写在Spring配置类上开启事务支持
- 配置事务管理相关的类TransactionConfig:
    ```java
    public class TransactionConfig {
        // 用于创建事务管理器对象
        @Bean(name = "transactionManager")
        public PlatformTransactionManager createTransactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }
    ```
  
- JdbcConfig.java:
```java
    public class JdbcConfig {
        @Value("${jdbc.driver}")
        private String driver;
        @Value("${jdbc.url}")
        private String url;
        @Value("${jdbc.username}")
        private String username;
        @Value("${jdbc.password}")
        private String password;
    
        // 创建JdbcTemplate对象
        @Bean(name = "jdbcTemplate")
        public JdbcTemplate createJdbcTemplate(DataSource dataSource){
            return new JdbcTemplate(dataSource);
        }
    
        // 创建一个数据源对象
        @Bean(name = "dataSource")
        public DataSource createDataSource(){
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            return ds;
        }
    }
```
- SpringConfig.java:
    ```java
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
    ```
