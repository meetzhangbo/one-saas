package com.zhangbo.onesaas.tenant.api.config;

import com.zhangbo.onesaas.tenant.api.contant.BeanIds;
import com.zhangbo.onesaas.tenant.api.multidatasource.DataSourceBasedMultiTenantConnectionProviderImpl;
import com.zhangbo.onesaas.tenant.api.multidatasource.TenantIdentifierResolverImpl;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

/**
 * @author zhangbo
 * @description:
 */
@DependsOn(BeanIds.MASTER_ENTITY_MANAGER_FACTORY_NAME)
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.zhangbo.onesaas.tenant.api.repository.tenant"},
        transactionManagerRef = "tenantTransactionManager",
        entityManagerFactoryRef = "tenantEntityManagerFactory"
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
    @Bean
    LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, multiIdentifierResolver);
        properties.put(Environment.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());


        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan("com.zhangbo.onesaas.tenant.api.entity.tenant");
        factory.setJpaProperties(properties);
        return factory;
    }

    @Bean
    public PlatformTransactionManager tenantTransactionManager() {
        return new JpaTransactionManager(tenantEntityManagerFactory().getObject());
    }

}
