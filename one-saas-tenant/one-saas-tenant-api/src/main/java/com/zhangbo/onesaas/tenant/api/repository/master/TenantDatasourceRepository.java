package com.zhangbo.onesaas.tenant.api.repository.master;

import com.zhangbo.onesaas.tenant.api.entity.master.TenantDatasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhangbo
 * @description:
 * @date 2020-07-29 12:06
 */
@Repository
public interface TenantDatasourceRepository extends JpaRepository<TenantDatasource, Long> {

    TenantDatasource findFirstByTenantId(Long tenantId);
}
