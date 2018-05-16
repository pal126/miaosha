package com.pal.miaosha.controller;

import com.pal.miaosha.domain.MiaoshaOrder;
import com.pal.miaosha.domain.OrderInfo;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.result.Result;
import com.pal.miaosha.service.GoodsService;
import com.pal.miaosha.service.MiaoshaService;
import com.pal.miaosha.service.OrderService;
import com.pal.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * 商品秒杀
     * @return
     */
    @RequestMapping(value = "/ms", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> ms(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user",user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //检查商品库存
        GoodsVo goodsVo = goodsService.getGoodsVo(goodsId);
        if (goodsVo.getStockCount() <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀
        MiaoshaOrder miaoshaOrder = orderService.getOrderByUserIdGoodsId(user.getId(),goodsVo.getId());
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下单 生成订单
        OrderInfo orderInfo = miaoshaService.order(user, goodsVo);
        return Result.success(orderInfo);
    }

}
