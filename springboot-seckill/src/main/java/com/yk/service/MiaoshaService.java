package com.yk.service;

import com.alibaba.fastjson.JSONObject;
import com.yk.pojo.*;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.MiaoshaKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service
public class MiaoshaService {

    private static final Logger logger = LoggerFactory.getLogger(MiaoshaService.class);

    @Resource
    private GoodsService goodsService;
    @Resource
    private RedisService redisService;

    @Resource
    private MiaoshaOrderService miaoshaOrderService;
    @Resource
    private MiaoshaGoodsService miaoshaGoodsService;


    public void handMsg(Message message) {
        try {
            message.acknowledge();
            MiaoshaMessage msg = null;
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                logger.info("listen2Miaosha msg = {}", message);
                msg = JSONObject.parseObject(text, MiaoshaMessage.class);
            }
            MiaoshaUser user = msg.getUser();
            long goodsId = msg.getGoodsId();
            OrderInfo orderinfo = this.getMiaoshaOrderByUserIdGoodsId(user, goodsId);
            logger.info("接受到秒杀消息 user={} goodsId={}", user, goodsId);


        } catch (JMSException e) {
            e.printStackTrace();
        }

    }




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

    @Transactional
    public OrderInfo getMiaoshaOrderByUserIdGoodsId(MiaoshaUser user, long goodsId) {
        // 减去库存、新增秒杀订单
        boolean flag = miaoshaGoodsService.reduceStock(goodsId);
        if(flag){
            GoodsVo goodsvo = goodsService.getGoodsVoByGoodsId(goodsId);
            OrderInfo orderinfo = miaoshaOrderService.createOrder(user,goodsvo);
            return orderinfo;
        }else {  //减少库存失败
            //做一个标记，代表商品已经秒杀完了。
            setGoodsOver(goodsId);
            return null;
        }
    }
}
