package com.pal.miaosha.controller;

import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.result.Result;
import com.pal.miaosha.service.UserService;
import com.pal.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    /**
     * 登陆
     * @param loginVo
     * @return
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> login(HttpServletResponse response, @Valid LoginVo loginVo) {
        userService.login(response, loginVo);
        return Result.success(true);
    }

}
