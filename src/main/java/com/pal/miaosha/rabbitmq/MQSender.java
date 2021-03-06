package com.pal.miaosha.rabbitmq;

import com.pal.miaosha.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * rabbitMQ Sender
 *
 * @author pal
 */
@Slf4j
@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 秒杀订单
     *
     * @param orderMessage
     */
    public void sendOrderMessage(OrderMessage orderMessage) {
        String msg = RedisService.beanToString(orderMessage);
        log.info("msg:{}", msg);
        amqpTemplate.convertAndSend(MQConfig.ORDER_QUEUE, msg);
    }

    /**
     * Direct
     *
     * @param message
     */
    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }

    /**
     * Topic
     *
     * @param message
     */
    public void sendTopic(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY1, msg + "1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY2, msg + "2");
    }

    /**
     * Fanout
     *
     * @param message
     */
    public void sendFanout(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, msg);
    }

    /**
     * Header
     *
     * @param message
     */
    public void sendHeader(Object message) {
        String msg = RedisService.beanToString(message);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("header1", "value1");
        messageProperties.setHeader("header2", "value2");
        Message obj = new Message(msg.getBytes(), messageProperties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
    }
}
