package com.pal.miaosha.rabbitmq;

import com.pal.miaosha.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMessage {
    private User user;
    private Long goodsId;

    public OrderMessage(User user, Long goodsId) {
        this.user = user;
        this.goodsId = goodsId;
    }
}
