package com.pal.miaosha.controller;

import com.pal.miaosha.domain.User;
import com.pal.miaosha.redis.GoodsKey;
import com.pal.miaosha.redis.RedisService;
import com.pal.miaosha.result.Result;
import com.pal.miaosha.service.GoodsService;
import com.pal.miaosha.service.UserService;
import com.pal.miaosha.vo.GoodsDetailVo;
import com.pal.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 商品列表页面
     *
     * @return String
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {

        model.addAttribute("user", user);
        //取redis缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (StringUtils.isNotEmpty(html)) {
            return html;
        }

        //查询商品列表
        List<GoodsVo> listGoodsVo = goodsService.listGoodsVo();
        model.addAttribute("listGoodsVo", listGoodsVo);
        //把model加入SpringWebContext
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        //spring boot渲染页面
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        //写入redis缓存
        if (StringUtils.isNotEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    /**
     * 商品详情页面
     *
     * @return Result
     */
    @RequestMapping(value = "/detail/{id}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(User user, @PathVariable("id") Long id) {
        //查询商品
        GoodsVo goodsVo = goodsService.getGoodsVo(id);
        //检查秒杀活动是否进行
        GoodsDetailVo goodsDetailVo = checkTime(user, goodsVo);
        return Result.success(goodsDetailVo);
    }

    /**
     * 检查秒杀活动是否进行
     *
     * @param goodsVo goodsVo
     * @return GoodsDetailVo
     */
    private GoodsDetailVo checkTime(User user, GoodsVo goodsVo) {
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            //秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setGoodsVo(goodsVo);
        return goodsDetailVo;
    }
}
