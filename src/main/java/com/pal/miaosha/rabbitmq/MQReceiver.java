package com.pal.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * rabbitMQ Receiver
 * @author pal
 * @date 2018/05/21
 */
@Slf4j
@Service
public class MQReceiver {

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receice(String message) {
        log.info("msg:{}",message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiceTopic1(String message) {
        log.info("msg:{}",message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiceTopic2(String message) {
        log.info("msg:{}",message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiceHeader(byte[] message) {
        log.info("msg:{}",message.toString());
    }

}
