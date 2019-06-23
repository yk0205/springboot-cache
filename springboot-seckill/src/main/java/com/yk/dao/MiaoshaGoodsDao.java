package com.yk.dao;

import com.yk.pojo.MiaoshaGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MiaoshaGoodsDao {

    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaGoods record);

    int insertSelective(MiaoshaGoods record);

    MiaoshaGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoshaGoods record);

    int updateByPrimaryKey(MiaoshaGoods record);
}