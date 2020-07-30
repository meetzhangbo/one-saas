package com.zhangbo.onesaas.tenant.api.multidatasource;

import com.zhangbo.onesaas.tenant.api.entity.master.TenantDatasource;
import com.zhangbo.onesaas.tenant.api.repository.master.TenantDatasourceRepository;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author zhangbo
 * @description:
 */
@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Autowired
    private TenantDatasourceRepository tenantDatasourceRepository;

    private Map<String, DataSource> dataSourceMap = new TreeMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        if (dataSourceMap.isEmpty()) {
            List<TenantDatasource> datasourceList = tenantDatasourceRepository.findAll();
            datasourceList.forEach(x -> {
                dataSourceMap.put(String.valueOf(x.getTenantDatasourceId()), dataSource(x));
            });
        }
        return dataSourceMap.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String s) {
        return dataSourceMap.get(s);
    }

    private DataSource dataSource(TenantDatasource tenantDatasource) {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(tenantDatasource.getUrl())
                .username(tenantDatasource.getUsername())
                .password(tenantDatasource.getPassword())
                .build();
    }

}
