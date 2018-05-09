package com.pal.miaosha.controller;

import com.pal.miaosha.domain.User;
import com.pal.miaosha.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

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
    public String toList(Model model,
                         @CookieValue(value = "token", required = false) String cookieToken,
                         @RequestParam(value = "token", required = false) String paramToken,
                         HttpServletResponse response) {

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        model.addAttribute("user",user);
        return "goods_list";
    }
}
