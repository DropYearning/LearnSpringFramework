package config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

/**
 * 该类是一个配置类，它的作用和bean.xml是一样的
 *  SpringConfig是一个总的配置类
 */

@Configuration
@ComponentScan(basePackages = {"com.study", "config"})
@Import(JdbcConfig.class)
@PropertySource("classpath:jdbcConfig.properties")
public class SpringConfig {


}
