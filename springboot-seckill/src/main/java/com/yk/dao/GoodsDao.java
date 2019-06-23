package com.yk.dao;

import com.yk.pojo.Goods;
import com.yk.pojo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDao {


    GoodsVo selectByPrimaryKey(Long id);
    List<GoodsVo> getGoodsVoList();


    int updateByPrimaryKeySelective(Goods record);
    int updateByPrimaryKeyWithBLOBs(Goods record);
    int updateByPrimaryKey(Goods record);
}