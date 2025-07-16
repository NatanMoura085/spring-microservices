package com.ead.course.consumers;

import com.ead.course.dtos.UserEventDTO;
import com.ead.course.enums.ActionType;
import com.ead.course.services.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    private UserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${broker.exchange.userEventExchange}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
    ))
    public void listenUserEvent(@Payload UserEventDTO userEventDTO) {
        try {
            if (userEventDTO.getActionType() == null) {
                throw new IllegalArgumentException("ActionType é nulo");
            }

            if (userEventDTO.getUserId() == null) {
                throw new IllegalArgumentException("UserId é nulo");
            }

            switch (ActionType.valueOf(userEventDTO.getActionType())) {
                case CREATE, UPDATE -> {
                    if (userEventDTO.getFullName() == null || userEventDTO.getFullName().isBlank()) {
                        System.out.println("Usuário ignorado. Campo 'fullName' está vazio ou nulo: " + userEventDTO);
                        return;
                    }
                    var userModel = userEventDTO.convertToUserModel();
                    userService.save(userModel);
                }
                case DELETE -> userService.delete(userEventDTO.getUserId());
            }

        } catch (Exception e) {
            // Não lança para o Rabbit reprocessar, apenas loga
            System.err.println("Erro ao processar UserEventDTO: " + userEventDTO);
            e.printStackTrace();
        }
    }
}
