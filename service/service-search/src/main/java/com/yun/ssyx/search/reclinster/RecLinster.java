package com.yun.ssyx.search.reclinster;


import com.rabbitmq.client.Channel;
import com.yun.ssyx.mq.constant.MqConst;
import com.yun.ssyx.search.service.SkuService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 商品上下架的监听消息发送
 */
@Component
public class RecLinster {
    @Autowired
    private SkuService skuService;

//    上架
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_UPPER), declare = "true",
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_UPPER}
    ))
    public void upperSku(Long skuId, Message message, Channel channel) {
        if (skuId != null) {
            skuService.upSkuProduct(skuId);
        }
//        消费端的手动确认
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    下架
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_LOWER), declare = "true",
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_LOWER}
    ))
    public void lowerSku(Long skuId, Message message, Channel channel) {
        if (skuId != null) {
            skuService.lowerSkuProduct(skuId);
        }

        //        消费端的手动确认
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
