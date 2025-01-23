package com.saproject.centerSA.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_TRANSACTIONS = "transactions_queue";
    public static final String EXCHANGE_TRANSACTIONS = "transactions_exchange";
    public static final String ROUTING_KEY = "transactions_routing_key";

    @Bean
    public Queue transactionsQueue() {
        System.out.println("Creating queue: " + QUEUE_TRANSACTIONS);
        return new Queue(QUEUE_TRANSACTIONS, true);
    }

    @Bean
    public DirectExchange transactionsExchange() {
        System.out.println("Creating exchange: " + EXCHANGE_TRANSACTIONS);
        return new DirectExchange(EXCHANGE_TRANSACTIONS);
    }

    @Bean
    public Binding transactionsBinding(Queue transactionsQueue, DirectExchange transactionsExchange) {
        System.out.println("Binding queue to exchange with routing key: " + ROUTING_KEY);
        return BindingBuilder.bind(transactionsQueue)
                .to(transactionsExchange)
                .with(ROUTING_KEY);
    }

}
