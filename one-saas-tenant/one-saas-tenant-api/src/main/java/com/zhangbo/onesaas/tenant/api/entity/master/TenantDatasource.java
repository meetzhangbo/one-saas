package com.zhangbo.onesaas.tenant.api.entity.master;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhangbo
 * @description:
 * @date 2020-07-29 11:29
 */
@Data
@Entity
public class TenantDatasource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenantDatasourceId;

    private String url;

    private String username;

    private String password;

    private Long tenantId;
}
