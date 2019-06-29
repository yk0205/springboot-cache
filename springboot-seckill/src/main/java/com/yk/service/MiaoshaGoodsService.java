package com.yk.service;

import com.yk.dao.MiaoshaGoodsDao;
import com.yk.dao.MiaoshaOrderDao;
import com.yk.dao.OrderInfoDao;
import com.yk.pojo.GoodsVo;
import com.yk.pojo.MiaoshaOrder;
import com.yk.pojo.MiaoshaUser;
import com.yk.pojo.OrderInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MiaoshaGoodsService {


    @Resource
    private MiaoshaGoodsDao miaoshaGoodsDao;

    public boolean reduceStock(GoodsVo goodsvo) {
        int i = miaoshaGoodsDao.reduceStock(goodsvo.getId());
        return i>0;

    }
    public boolean reduceStock(long goodsId) {
        int i = miaoshaGoodsDao.reduceStock(goodsId);
        return i>0;

    }
}
