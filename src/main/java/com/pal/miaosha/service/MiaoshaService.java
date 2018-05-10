package com.pal.miaosha.service;

import com.pal.miaosha.domain.OrderInfo;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    /**
     * 秒杀活动下单
     * @param user
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo order(User user, GoodsVo goodsVo) {
        //减库存
        goodsService.reduceStock(goodsVo);
        //下订单
        return orderService.createOrder(user, goodsVo);
    }

}
