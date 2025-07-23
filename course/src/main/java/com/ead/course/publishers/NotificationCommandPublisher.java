package com.ead.course.publishers;

import com.ead.course.dtos.NotificationComandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.exchange.notificationCommandExchange}")
    private String notificationCommandExchange;

    @Value(value = "${broker.key.notificationCommandKey}")
    private String notificationCommandKey;


    public void publishNotificationCommand(NotificationComandDto notificationComandDto){
        rabbitTemplate.convertAndSend(notificationCommandExchange,notificationCommandKey,notificationComandDto);
    }
}
