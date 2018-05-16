package com.pal.miaosha.vo;

import com.pal.miaosha.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsDetailVo {
    private int miaoshaStatus;
    private int remainSeconds;
    private GoodsVo goodsVo;
    private User user;
}
