package com.yk.activemq;


import com.google.common.collect.Maps;
import com.yk.service.MiaoshaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.jms.Message;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class ActiveMqConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMqConsumer.class);

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * 多线程消费
     */
    private ExecutorService threadPool = new ThreadPoolExecutor(10, 20, 0L,
               TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1),(x,y)->{
        try {
            y.getQueue().put(x);
        } catch (InterruptedException e) {
            logger.error("threadPool failed  ",e);
        }
    });


    @JmsListener(destination = "miaosha")
    public void listen2Miaosha(Message message) {
        logger.info("当前线程 {} " ,Thread.currentThread().getName());
        threadPool.execute(() -> miaoshaService.handMsg(message));
        Map map = Maps.newHashMap();
    }


}
