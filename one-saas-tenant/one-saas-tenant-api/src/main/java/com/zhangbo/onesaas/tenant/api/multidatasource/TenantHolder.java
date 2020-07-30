package com.zhangbo.onesaas.tenant.api.multidatasource;


/**
 * @author zhangbo
 */
public final class TenantHolder {

    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    public static void set(Long id) {
        TENANT_ID.set(id);
    }

    public static Long get() {
        return TENANT_ID.get();
    }

    public static void clear() {
        TENANT_ID.remove();
    }
}
