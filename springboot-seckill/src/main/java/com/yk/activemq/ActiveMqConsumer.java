package com.yk.activemq;


import com.yk.service.MiaoshaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.jms.Message;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ActiveMqConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMqConsumer.class);

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * 多线程消费
     */
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1));


    @JmsListener(destination = "miaosha")
    public void listen2Miaosha(Message message) {
        threadPool.submit(() -> miaoshaService.handMsg(message));
    }


}
