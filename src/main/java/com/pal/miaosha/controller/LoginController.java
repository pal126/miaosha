package com.pal.miaosha.controller;

import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/tologin")
    public String toLogin(Model model) {
        return "login";
    }

}
