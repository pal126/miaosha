package com.pal.miaosha.vo;

import com.pal.miaosha.domain.OrderInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailVo {
    private OrderInfo orderInfo;
    private GoodsVo goodsVo;
}
