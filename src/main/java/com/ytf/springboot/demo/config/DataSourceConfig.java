package com.ytf.springboot.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:application.yml")
@MapperScan(basePackages = { "com.ytf.springboot.demo.dao" }, sqlSessionFactoryRef = "SpringDemoSqlSessionFactory")
public class DataSourceConfig {

    @Value("${spring.datasource.master.url}")
    private String dbUrl;
    @Value("${spring.datasource.master.username}")
    private String dbUser;
    @Value("${spring.datasource.master.password}")
    private String dbPassword;
    @Value("${spring.datasource.master.driver-class-name}")
    private String driverName;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean(name = "SpringDemoDataSource")
    public DataSource SpringDemoDataSource() {
        //TODO 配置拉出去，应对大的TPS
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxWait(60000);
        dataSource.setMaxActive(20);
        dataSource.setInitialSize(10);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        dataSource.setMinIdle(10);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestOnBorrow(false);
        return dataSource;
    }

    @Bean(name = "SpringDemoTransactionManager")
    public DataSourceTransactionManager partnerTransactionManager() {
        return new DataSourceTransactionManager(SpringDemoDataSource());
    }

    @Bean(name = "SpringDemoSqlSessionFactory")
    public SqlSessionFactory SpringDemoSqlSessionFactory(@Qualifier("SpringDemoDataSource") DataSource partnerSettlementDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(partnerSettlementDataSource);
        sessionFactory.setMapperLocations(
                ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath:mapper/*.xml"));
        return sessionFactory.getObject();
    }
}