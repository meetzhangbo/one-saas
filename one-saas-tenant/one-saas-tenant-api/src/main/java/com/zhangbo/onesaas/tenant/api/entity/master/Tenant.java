package com.zhangbo.onesaas.tenant.api.entity.master;


import lombok.Data;

import javax.persistence.*;

/**
 * 租户信息
 * @author zhangbo
 */
@Data
@Entity
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenant_id;

    private String username;

    private String password;

}
