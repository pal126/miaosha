package com.pal.miaosha.controller;

import com.pal.miaosha.domain.User;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.service.GoodsService;
import com.pal.miaosha.service.UserService;
import com.pal.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    /**
     * 商品列表页面
     * @return
     */
    @RequestMapping("/to_list")
    public String toList(Model model, User user) {
        model.addAttribute("user",user);
        //查询商品列表
        List<GoodsVo> listGoodsVo = goodsService.listGoodsVo();
        model.addAttribute("listGoodsVo",listGoodsVo);
        return "goods_list";
    }

    /**
     * 商品详情页面
     * @return
     */
    @RequestMapping("/to_detail/{id}")
    public String toDetail(Model model, User user, @PathVariable("id") Long id) {
        model.addAttribute("user",user);
        //查询商品
        GoodsVo goodsVo = goodsService.getGoodsVo(id);
        model.addAttribute("goodsVo",goodsVo);

        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
