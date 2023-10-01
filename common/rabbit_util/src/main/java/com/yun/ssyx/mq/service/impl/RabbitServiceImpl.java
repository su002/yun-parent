package com.yun.ssyx.mq.service.impl;

import com.yun.ssyx.mq.service.RabbitService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitServiceImpl implements RabbitService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /** 消息发生方法
     * exchange : 交换机
     * routeKey: 路由
     * msg:      发送的消息
     * @param exchange
     * @param routeKey
     * @param msg
     */
    @Override
    public boolean sendMessage(String exchange, String routeKey, Object msg) {
        rabbitTemplate.convertAndSend(exchange, routeKey, msg);
        return true;
    }



}
