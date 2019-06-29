package com.yk.activemq;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.yk.pojo.MiaoshaMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;


@Service
public class ActiveMqProducer {

    private static final String MIAOSHA_QUEUE = "miaosha";


    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void sendMsg(String destinationName ,String message){
        System.out.println("------------ 发送消息"+ message);
        Destination destination = new ActiveMQQueue(destinationName);
        jmsTemplate.convertAndSend(destination,message);
    }

    public void sendMsg2Miaosha(MiaoshaMessage msg) {
        Destination destination = new ActiveMQQueue(MIAOSHA_QUEUE);
        String str= JSON.toJSONString(msg);
        jmsTemplate.convertAndSend(destination, str);
    }
}
