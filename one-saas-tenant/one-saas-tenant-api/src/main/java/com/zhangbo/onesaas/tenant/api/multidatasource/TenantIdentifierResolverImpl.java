package com.zhangbo.onesaas.tenant.api.multidatasource;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;


/**
 * 获取当前租户ID，用于获取数据源
 *
 * @author zhangbo
 * @description:
 */
@Component
public class TenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT = "default";

    @Override
    public String resolveCurrentTenantIdentifier() {
        return TenantHolder.get() == null ? DEFAULT_TENANT : String.valueOf(TenantHolder.get());
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}
