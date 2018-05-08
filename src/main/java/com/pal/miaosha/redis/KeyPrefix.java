package com.pal.miaosha.redis;

public interface KeyPrefix {
    int expireSeconds();
    String getPrefix();
}
