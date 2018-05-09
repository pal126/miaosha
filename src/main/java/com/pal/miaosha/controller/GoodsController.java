package com.pal.miaosha.controller;

import com.pal.miaosha.domain.User;
import com.pal.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    /**
     * 商品页面
     * @return
     */
    @RequestMapping("/to_list")
    public String toList(Model model, User user) {
        model.addAttribute("user",user);
        return "goods_list";
    }
}
