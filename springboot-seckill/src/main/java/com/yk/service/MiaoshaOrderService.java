package com.yk.service;

import com.yk.dao.MiaoshaOrderDao;
import com.yk.dao.OrderInfoDao;
import com.yk.pojo.*;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.OrderKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MiaoshaOrderService {

    @Resource
    private MiaoshaOrderDao miaoshaOrderDao;
    @Resource
    private OrderInfoDao orderInfoDao;
    @Resource
    private RedisService redisService;



    public MiaoshaOrder getMiaoshaOrderByUserIdAndCoodsId(Long userid, long goodsId) {
        MiaoshaOrder order = miaoshaOrderDao.getMiaoshaOrderByUserIdAndCoodsId(userid, goodsId);
        return order;
    }


    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsvo) {
        //1.生成order_info
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setDeliveryAddrId(0L);//long类型 private Long deliveryAddrId;   L
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsvo.getId());
        orderInfo.setGoodsName(goodsvo.getGoodsName());
        //秒杀价格
        orderInfo.setGoodsPrice(goodsvo.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        //订单状态  ---0-新建未支付  1-已支付  2-已发货  3-已收货
        orderInfo.setOrderStatus(0);
        //用户id
        orderInfo.setUserId(user.getId());
        //返回orderId
        //long orderId=
        orderInfoDao.insert(orderInfo);
        //2.生成miaosha_order
        MiaoshaOrder miaoshaorder =new MiaoshaOrder();
        miaoshaorder.setGoodsId(goodsvo.getId());
        //将订单id传给秒杀订单里面的订单orderid
        miaoshaorder.setOrderId(orderInfo.getId());
        miaoshaorder.setUserId(user.getId());
        miaoshaOrderDao.insert(miaoshaorder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getId()+"_"+goodsvo.getId(),miaoshaorder);

        return orderInfo;
    }

}
