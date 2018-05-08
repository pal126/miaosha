package com.pal.miaosha.controller;

import com.pal.miaosha.result.Result;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.redis.UserKey;
import com.pal.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name","pal");
        return "hello";
    }

    @RequestMapping("/user")
    @ResponseBody
    public Result getById() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/redisGet")
    @ResponseBody
    public Result<User> getRedis() {
        User user = redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user);
    }

    @RequestMapping("/redisSet")
    @ResponseBody
    public Result<Boolean> setRedis() {
        User user = new User();
        boolean flag = redisService.set(UserKey.getById,""+1,user);
        return Result.success(flag);
    }

}
