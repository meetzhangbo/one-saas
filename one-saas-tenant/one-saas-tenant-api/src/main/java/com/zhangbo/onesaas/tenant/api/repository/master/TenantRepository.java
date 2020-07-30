package com.zhangbo.onesaas.tenant.api.repository.master;

import com.zhangbo.onesaas.tenant.api.entity.master.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhangbo
 * @description:
 * @date 2020-07-29 12:03
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
