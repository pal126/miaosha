package com.pal.miaosha.redis;

public abstract class BasePrefix implements KeyPrefix {

    /**
     * 0代表永不过期
     */
    private int expireSeconds;

    /**
     * key前缀
     */
    private String prefix;

    public BasePrefix(String prefix) {
        this.expireSeconds = 0;
        this.prefix = prefix;
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
