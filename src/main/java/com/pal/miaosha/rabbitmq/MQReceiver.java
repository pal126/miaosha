package com.pal.miaosha.rabbitmq;

import com.pal.miaosha.domain.MiaoshaOrder;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.result.Result;
import com.pal.miaosha.service.GoodsService;
import com.pal.miaosha.service.MiaoshaService;
import com.pal.miaosha.service.OrderService;
import com.pal.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * rabbitMQ Receiver
 * @author pal
 * @date 2018/05/21
 */
@Slf4j
@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * 秒杀订单
     * @param message
     */
    @RabbitListener(queues = MQConfig.ORDER_QUEUE)
    public void receiceOrder(String message) {
        OrderMessage msg = RedisService.stringToBean(message, OrderMessage.class);
        User user = msg.getUser();
        Long goodsId = msg.getGoodsId();
        //检查商品库存
        GoodsVo goodsVo = goodsService.getGoodsVo(goodsId);
        if (goodsVo.getStockCount() <= 0) {
            return;
        }
        //判断是否已经秒杀
        MiaoshaOrder miaoshaOrder = orderService.getOrderByUserIdGoodsId(user.getId(),goodsVo.getId());
        if (miaoshaOrder != null) {
            return;
        }
        //减库存 下单 生成订单
        miaoshaService.order(user, goodsVo);
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receice(String message) {
        log.info("msg:{}",message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiceTopic1(String message) {
        log.info("msg:{}",message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiceTopic2(String message) {
        log.info("msg:{}",message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiceHeader(byte[] message) {
        log.info("msg:{}",message.toString());
    }

}
