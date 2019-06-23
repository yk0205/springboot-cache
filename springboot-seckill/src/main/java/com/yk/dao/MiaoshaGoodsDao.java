package com.yk.dao;

import com.yk.pojo.MiaoshaGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MiaoshaGoodsDao {

    int insert(MiaoshaGoods record);

    MiaoshaGoods selectByPrimaryKey(Long id);

    int reduceStock(Long id);


}