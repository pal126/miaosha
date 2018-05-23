package com.pal.miaosha.service;

import com.pal.miaosha.domain.MiaoshaOrder;
import com.pal.miaosha.domain.OrderInfo;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.redis.GoodsKey;
import com.pal.miaosha.redis.RedisService;
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

    @Autowired
    RedisService redisService;

    /**
     * long 成功
     * -1 秒杀失败
     * 0  正在排队
     * @param userId
     * @param goodsId
     * @return
     */
    public Long getResult(Long userId, Long goodsId) {
        MiaoshaOrder miaoshaOrder = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (miaoshaOrder != null) {
            return miaoshaOrder.getOrderId();
        } else {
            boolean flag = getGoodsOver(goodsId);
            if (flag) {
                return -1L;
            } else {
                return 0L;
            }
        }
    }

    /**
     * 秒杀活动下单
     * @param user
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo order(User user, GoodsVo goodsVo) {
        //减库存
        boolean flag = goodsService.reduceStock(goodsVo);
        if (flag) {
            return orderService.createOrder(user, goodsVo);
        }
        setGoodsOver(goodsVo.getId());
        return null;
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(GoodsKey.getBooleanStock, "" + goodsId, true);
    }

    private boolean getGoodsOver(Long goodsId) {
        return redisService.exist(GoodsKey.getBooleanStock, "" + goodsId);
    }

}
