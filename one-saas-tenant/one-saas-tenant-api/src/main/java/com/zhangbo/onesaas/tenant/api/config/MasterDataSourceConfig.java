package com.zhangbo.onesaas.tenant.api.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zhangbo.onesaas.tenant.api.contant.BeanIds;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author zhangbo
 * @description:
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.zhangbo.onesaas.tenant.api.repository.master"},
        transactionManagerRef = "masterTransactionManager",
        entityManagerFactoryRef = "masterEntityManagerFactory")
@EnableTransactionManagement
public class MasterDataSourceConfig {

    @Autowired
    private MasterDatasourceProperties masterDatasourceProperties;

    @Bean
    DataSource masterDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(masterDatasourceProperties.getDriverClassName());
        hikariDataSource.setUsername(masterDatasourceProperties.getUsername());
        hikariDataSource.setPassword(masterDatasourceProperties.getPassword());
        hikariDataSource.setJdbcUrl(masterDatasourceProperties.getUrl());
        return hikariDataSource;
    }

    @Bean
    PlatformTransactionManager masterTransactionManager() {
        return new JpaTransactionManager(masterEntityManagerFactory().getObject());
    }

    @Bean
    LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabasePlatform("");
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());

        factoryBean.setPackagesToScan("com.zhangbo.onesaas.tenant.api.entity.master");
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(properties);
        factoryBean.setDataSource(masterDataSource());

        return factoryBean;
    }


}
