package com.pal.miaosha.redis;

public class UserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600;

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk");
    public static UserKey getById = new UserKey(0, "id");
}
