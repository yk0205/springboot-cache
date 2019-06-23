package com.yk.service;

import com.yk.dao.GoodsDao;
import com.yk.pojo.Goods;
import com.yk.pojo.GoodsVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class GoodsService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    private GoodsDao goodsDao;

    public List<GoodsVo> getGoodsVoList() {
        List<GoodsVo> goodsVoList = goodsDao.getGoodsVoList();
        return goodsVoList;
    }

    public Goods getGoodsById(long goodsId) {
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        return goods;
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        GoodsVo goodsVo = goodsDao.selectByPrimaryKey(goodsId);
        return goodsVo;
    }
}
