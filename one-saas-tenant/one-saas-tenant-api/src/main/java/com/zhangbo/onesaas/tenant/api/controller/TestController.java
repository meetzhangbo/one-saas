package com.zhangbo.onesaas.tenant.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangbo
 * @description:
 * @date 2020-07-28 14:32
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "hahah";
    }
}
