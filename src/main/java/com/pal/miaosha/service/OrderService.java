package com.pal.miaosha.service;

import com.pal.miaosha.dao.OrderDao;
import com.pal.miaosha.domain.MiaoshaOrder;
import com.pal.miaosha.domain.OrderInfo;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.redis.OrderKey;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    /**
     * 判断是否已经秒杀
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public MiaoshaOrder getOrderByUserIdGoodsId(Long userId, Long goodsId) {
        return redisService.get(OrderKey.getOrderByUidGid, "" + userId + "_" + goodsId, MiaoshaOrder.class);
    }

    public OrderInfo getOrderById(Long orderId) {
        return orderDao.getOrderById(orderId);
    }

    /**
     * 下订单
     *
     * @param user
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(user.getId());
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo.setStatus(0);
        orderDao.insertOrder(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getOrderByUidGid, "" + user.getId() + "_" + goodsVo.getId(), miaoshaOrder);
        return orderInfo;
    }

}
