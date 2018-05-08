package com.pal.miaosha.redis;

public class OrderKey extends BasePrefix {

    private OrderKey(String prefix) {
        super(prefix);
    }

    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
