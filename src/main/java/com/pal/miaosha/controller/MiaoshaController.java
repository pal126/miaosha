package com.pal.miaosha.controller;

import com.pal.miaosha.domain.User;
import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.service.GoodsService;
import com.pal.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    /**
     * 商品秒杀
     * @return
     */
    @RequestMapping("/ms/{id}")
    public String ms(Model model, User user, @PathVariable("id") Long id) {
        model.addAttribute("user",user);
        if (user == null) {
            return "login";
        }
        //检查库存
        GoodsVo goodsVo = goodsService.getGoodsVo(id);
        if (goodsVo.getStockCount() <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER);
            return "miaosha_fail";
        }
        return "goods_list";
    }

}
