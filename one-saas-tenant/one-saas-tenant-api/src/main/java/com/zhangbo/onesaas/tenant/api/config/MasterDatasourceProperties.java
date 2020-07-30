package com.zhangbo.onesaas.tenant.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author zhangbo
 * @description:
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class MasterDatasourceProperties {
    private String driverClassName;

    private String username;

    private String password;

    private String url;
}
