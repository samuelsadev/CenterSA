package com.saproject.centerSA.service;

import com.saproject.centerSA.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_TRANSACTIONS,
                    RabbitMQConfig.ROUTING_KEY,
                    message
            );
            System.out.println("Message sent to RabbitMQ: " + message);
        } catch (Exception e) {
            System.err.println("Failed to send message to RabbitMQ: " + e.getMessage());
        }
    }
}
