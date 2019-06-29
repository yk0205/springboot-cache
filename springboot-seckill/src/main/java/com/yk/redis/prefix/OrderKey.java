package com.yk.redis.prefix;


public abstract class OrderKey extends BasePrefix{

    public static final int TOKEN_EXPIRE=3600*24*2;//3600S*24*2    =2天

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey getMiaoshaOrderByUidGid =new MiaoshaUserKey(TOKEN_EXPIRE,"mo");



}
