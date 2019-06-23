package com.yk.redis.prefix;


public abstract class OrderKey implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public OrderKey(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {  //  默认0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        return  prefix;
    }
}
