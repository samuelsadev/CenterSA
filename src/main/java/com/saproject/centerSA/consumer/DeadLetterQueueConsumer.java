package com.saproject.centerSA.consumer;

import com.saproject.centerSA.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DLQ)
    public void receiveDeadLetter(@Payload String failedMessage) {
        System.err.println("Message sent to DLQ: " + failedMessage);
    }
}
