package com.pal.miaosha.redis;

public class OrderKey extends BasePrefix {

    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getOrderByUidGid = new OrderKey(0, "oug");
}
