package com.zhangbo.onesaas.tenant.api.controller;

import com.zhangbo.onesaas.tenant.api.entity.tenant.User;
import com.zhangbo.onesaas.tenant.api.repository.tenant.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangbo
 * @description:
 * @date 2020-07-28 14:32
 */
@RestController
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public List<User> test() {
        List<User> all = userRepository.findAll();
        return all;
    }
}
