package com.pal.miaosha.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receice(String message) {

    }

}
