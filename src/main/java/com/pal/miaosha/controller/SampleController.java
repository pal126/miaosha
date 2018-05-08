package com.pal.miaosha.controller;

import com.pal.miaosha.domain.User;
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

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name","pal");
        return "hello";
    }

    @RequestMapping("/user")
    @ResponseBody
    public User getById() {
        return userService.getById(1);
    }

}
