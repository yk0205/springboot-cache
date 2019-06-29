package com.yk.controller;

import com.yk.activemq.ActiveMqProducer;
import com.yk.pojo.GoodsVo;
import com.yk.pojo.MiaoshaMessage;
import com.yk.pojo.MiaoshaOrder;
import com.yk.pojo.MiaoshaUser;
import com.yk.redis.RedisService;
import com.yk.redis.prefix.GoodsKey;
import com.yk.result.CodeMsg;
import com.yk.result.Result;
import com.yk.service.GoodsService;
import com.yk.service.MiaoshaOrderService;
import com.yk.service.MiaoshaService;
import com.yk.service.MiaoshaUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@RequestMapping("/miaosha")
@Controller
public class MiaoshaController  implements InitializingBean {

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoshaOrderService miaoshaOrderService;

    @Autowired
    ActiveMqProducer activeMqProducer;


    /**
     * 初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 查询所有秒杀的商品，如果是null返回，否则将所有的商品放入缓存
        List<GoodsVo> list = goodsService.getGoodsVoList();
        if(Collections.EMPTY_LIST.equals(list)){
            return;
        }
        for (GoodsVo goods : list){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());
        }
    }

    @RequestMapping("/do_miaosha")
    @ResponseBody
    public Result<Object> toList(Model model, MiaoshaUser user,
                                 @RequestParam("goodsId") long goodsId) {
        //如果用户为空，则返回至登录页面
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 减去库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(stock<0){
            return Result.error(CodeMsg.MIAOSHA_OVER_ERROR);
        }

        // 判断是否秒杀到了
        MiaoshaOrder order = miaoshaOrderService.getMiaoshaOrderByUserIdAndCoodsId(user.getId(), goodsId);
        if (Objects.nonNull(order)) {//重复下单
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        MiaoshaMessage msg = new MiaoshaMessage();
        msg.setGoodsId(goodsId);
        msg.setUser(user);
        activeMqProducer.sendMsg2Miaosha(msg);
        return Result.success(0);

//        GoodsVo goodsvo = goodsService.getGoodsVoByGoodsId(goodsId);
//        //判断商品库存，库存大于0，才进行操作，多线程下会出错
//        int stockcount = goodsvo.getStockCount();
//        if (stockcount <= 0) {//失败			库存至临界值1的时候，此时刚好来了加入10个线程，那么库存就会-10
//            model.addAttribute("errorMessage", CodeMsg.MIAOSHA_OVER_ERROR);
//            return "miaosha_fail";
//        }
//        //判断这个秒杀订单形成没有，判断是否已经秒杀到了，避免一个账户秒杀多个商品

//        //可以秒杀，原子操作：1.库存减1，2.下订单，3.写入秒杀订单--->是一个事务
//        OrderInfo orderinfo=miaoshaService.miaosha(user,goodsvo);

//        model.addAttribute("goods", goodsvo);
//        model.addAttribute("orderinfo", orderinfo);
//        model.addAttribute("user", user);
//        return "order_detail";//返回支付页面
    }



}
