package com.yk.controller;

import com.yk.pojo.Goods;
import com.yk.pojo.GoodsVo;
import com.yk.pojo.MiaoshaUser;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.GoodsKey;
import com.yk.service.GoodsService;
import com.yk.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletResponse response, HttpServletRequest request, Model model, MiaoshaUser user) {
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsList);

        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isBlank(html)) {
            return html;
        }

        // 静态页面
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isBlank(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }

        model.addAttribute("user", user);
        return html;
    }


    @ResponseBody
    @RequestMapping(value ="/to_detail/{goodsId}",produces = "text/html")
    public String detail(HttpServletResponse response, HttpServletRequest request,Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {

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

        String html = redisService.get(GoodsKey.getGoodsDetail, "", String.class);
        if (!StringUtils.isBlank(html)) {
            return html;
        }

        // 静态页面
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isBlank(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

}
