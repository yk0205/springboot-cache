package com.yk.pojo;

import java.io.Serializable;

public class MiaoshaMessage implements Serializable {


    private static final long serialVersionUID = 1L;

    private  MiaoshaUser user ;
    private long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
