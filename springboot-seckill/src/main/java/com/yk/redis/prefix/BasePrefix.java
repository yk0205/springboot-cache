package com.yk.redis.prefix;

public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }
    public BasePrefix( String prefix) {   //0 代表永不过期
        this(0,prefix);
    }

    @Override
    public int expireSeconds() {  //  默认0代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String classname = getClass().getSimpleName();
        return classname + ":" + prefix;
    }
}
