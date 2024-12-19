package com.qiu.apiinterface.controller;


import com.qiu.apiclientsdk.model.User;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @description: 测试接口控制类
 * @className: NameController.java
 * @author: qiu
 * @createTime: 2024/3/14 16:08
 */
@RestController
@RequestMapping("/name")
@Api(tags = "测试接口")
public class NameController {

    @GetMapping("/")
    public String getName(@RequestParam String name) {
        return name;
    }

    @PostMapping("/post")
    public String postName(@RequestParam String name) {
        return name;
    }

    @PostMapping("/body")
    public User postNameBody(@RequestBody User user, HttpServletRequest request) {
        return user;
    }

}
