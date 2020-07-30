package com.zhangbo.onesaas.tenant.api.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zhangbo.onesaas.tenant.api.contant.BeanIds;
import org.hibernate.cfg.Environment;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author zhangbo
 * @description:
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.zhangbo.onesaas.tenant.api.repository.master"},
        transactionManagerRef = BeanIds.MASTER_TRANSACTION_MANAGER,
        entityManagerFactoryRef = BeanIds.MASTER_ENTITY_MANAGER_FACTORY_NAME)
@EnableTransactionManagement
public class MasterDataSourceConfig {

    @Autowired
    private MasterDatasourceProperties masterDatasourceProperties;

    /**
     * 配置主数据源
     *
     * @return
     */
    @Primary
    @Bean(name = BeanIds.MASTER_DATASOURCE_NAME)
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(masterDatasourceProperties.getDriverClassName());
        hikariDataSource.setUsername(masterDatasourceProperties.getUsername());
        hikariDataSource.setPassword(masterDatasourceProperties.getPassword());
        hikariDataSource.setJdbcUrl(masterDatasourceProperties.getUrl());
        return hikariDataSource;
    }

    /**
     * JPA工厂
     *
     * @return
     */
    @Primary
    @Bean(name = BeanIds.MASTER_ENTITY_MANAGER_FACTORY_NAME)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(hibernateJpaVendorAdapter());
        factory.setDataSource(dataSource());
        factory.setPersistenceUnitName("one-saas-master-unit");
        factory.setPackagesToScan("com.zhangbo.onesaas.tenant.api.entity.master");

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.FORMAT_SQL, true);
        properties.put("spring.jpa.hibernate.naming-strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        factory.setJpaProperties(properties);

        return factory;
    }

    /**
     * 指定JPA实现厂商
     *
     * @return
     */
    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    /**
     * 主数据源事务管理器
     *
     * @param emf
     * @return
     */
    @Primary
    @Bean(name = BeanIds.MASTER_TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(@Qualifier(BeanIds.MASTER_ENTITY_MANAGER_FACTORY_NAME) EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }


}
