package com.yk.controller;

import com.yk.pojo.Goods;
import com.yk.pojo.GoodsVo;
import com.yk.pojo.MiaoshaUser;
import com.yk.redis.RedisService;
import com.yk.service.GoodsService;
import com.yk.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RequestMapping("/goods")
@Controller
public class GoodsController {

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model, MiaoshaUser user) {
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("user", user);
        return "goods_list";
    }


    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int status = 0;  // 0 未开始 1 开始 2 结束
        int remailSeconds = 0;  // 倒计时
        if (startAt > now) { // 开始时间大于现在时间
            remailSeconds = (int) (startAt - now) / 1000;
        } else if (endAt < now) {
            status = 2;
            remailSeconds = -1;
        } else {
            status = 1;
        }

        model.addAttribute("goods", goods);
        model.addAttribute("status", status);
        model.addAttribute("remailSeconds", remailSeconds);
        model.addAttribute("user", user);

        return "goods_detail";
    }

}
