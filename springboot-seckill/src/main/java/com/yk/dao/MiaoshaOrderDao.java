package com.yk.dao;

import com.yk.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MiaoshaOrderDao {

    MiaoshaOrder getMiaoshaOrderByUserIdAndCoodsId(@Param("userid") Long userid, @Param("goodsId") long goodsId);

    int insert(MiaoshaOrder record);

    MiaoshaOrder selectByPrimaryKey(Long id);

    int reduceStock(Long id);


}