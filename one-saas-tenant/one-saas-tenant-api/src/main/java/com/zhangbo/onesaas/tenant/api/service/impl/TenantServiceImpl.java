package com.zhangbo.onesaas.tenant.api.service.impl;


import com.zhangbo.onesaas.tenant.api.repository.master.TenantRepository;
import com.zhangbo.onesaas.tenant.api.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangbo
 * @description:
 * @date 2020-07-29 12:12
 */
@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;


}
