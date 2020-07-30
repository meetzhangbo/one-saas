package com.zhangbo.onesaas.tenant.api.config;

import com.zhangbo.onesaas.tenant.api.contant.BeanIds;
import com.zhangbo.onesaas.tenant.api.multidatasource.DataSourceBasedMultiTenantConnectionProviderImpl;
import com.zhangbo.onesaas.tenant.api.multidatasource.TenantIdentifierResolverImpl;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * @author zhangbo
 * @description:
 */
@DependsOn(BeanIds.MASTER_ENTITY_MANAGER_FACTORY_NAME)
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.zhangbo.onesaas.tenant.api.repository.tenant"},
        transactionManagerRef = BeanIds.TENANT_TRANSACTION_MANAGER,
        entityManagerFactoryRef = BeanIds.TENANT_ENTITY_MANAGER_FACTORY_NAME
)
public class MultiDatasourceConfig {

    @Autowired
    private DataSourceBasedMultiTenantConnectionProviderImpl multiTenantConnectionProvider;

    @Autowired
    private TenantIdentifierResolverImpl multiIdentifierResolver;

    /**
     * 多数据源JPA工厂
     *
     * @return
     */
    @Bean(name = BeanIds.TENANT_ENTITY_MANAGER_FACTORY_NAME)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPersistenceUnitName("one-saas-tenant-unit");
        factory.setPackagesToScan("com.zhangbo.onesaas.tenant.api.entity.tenant");

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, multiIdentifierResolver);
        properties.put(Environment.PHYSICAL_NAMING_STRATEGY, "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        factory.setJpaProperties(properties);
        return factory;
    }


    /**
     * 多数据源事务管理器
     *
     * @param managerFactory
     * @return
     */
    @Bean(name = BeanIds.TENANT_TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier(BeanIds.TENANT_ENTITY_MANAGER_FACTORY_NAME) EntityManagerFactory managerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(managerFactory);
        return jpaTransactionManager;
    }

}
