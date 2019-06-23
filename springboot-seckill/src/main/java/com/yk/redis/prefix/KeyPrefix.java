package com.yk.redis.prefix;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}
