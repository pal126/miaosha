package com.pal.miaosha.controller;

import com.pal.miaosha.domain.MiaoshaOrder;
import com.pal.miaosha.domain.OrderInfo;
import com.pal.miaosha.domain.User;
import com.pal.miaosha.rabbitmq.MQSender;
import com.pal.miaosha.rabbitmq.OrderMessage;
import com.pal.miaosha.redis.GoodsKey;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.result.CodeMsg;
import com.pal.miaosha.result.Result;
import com.pal.miaosha.service.GoodsService;
import com.pal.miaosha.service.MiaoshaService;
import com.pal.miaosha.service.OrderService;
import com.pal.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if (goodsVos == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(GoodsKey.getGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    /**
     * 客户端轮询下单结果
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Long result = miaoshaService.getResult(user.getId(), goodsId);
        return Result.success(result);
    }
    /**
     * 商品秒杀
     * @return
     */
    @RequestMapping(value = "/ms", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> ms(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user",user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //商品库存标记在内存,减少对redis的访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //检查商品库存
        Long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否已经秒杀
        MiaoshaOrder miaoshaOrder = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //rabbitMQ入队
        OrderMessage orderMessage = new OrderMessage(user, goodsId);
        mqSender.sendOrderMessage(orderMessage);
        return Result.success(0);

        /*
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
        */
    }

}
