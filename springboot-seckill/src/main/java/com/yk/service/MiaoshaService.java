package com.yk.service;

import com.yk.dao.GoodsDao;
import com.yk.pojo.GoodsVo;
import com.yk.pojo.MiaoshaUser;
import com.yk.pojo.OrderInfo;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.MiaoshaKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MiaoshaService {


    @Resource
    private GoodsService goodsService;
    @Resource
    private RedisService redisService;

    @Resource
    private MiaoshaOrderService miaoshaOrderService;
    @Resource
    private MiaoshaGoodsService miaoshaGoodsService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 减去库存、新增秒杀订单
        boolean flag = miaoshaGoodsService.reduceStock(goods);
        if(flag){
            OrderInfo orderinfo = miaoshaOrderService.createOrder(user,goods);
            return orderinfo;
        }else {  //减少库存失败
            //做一个标记，代表商品已经秒杀完了。
            setGoodsOver(goods.getId());
            return null;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);

    }
}
