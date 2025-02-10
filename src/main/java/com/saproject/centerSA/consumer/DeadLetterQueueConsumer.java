package com.saproject.centerSA.consumer;

import com.saproject.centerSA.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = RabbitMQConfig.DLQ_TRANSACTIONS)
    public void receiveDeadLetter(@Payload String failedMessage) {
        System.err.println("Mensage send to DLQ: " + failedMessage);
    }
}
