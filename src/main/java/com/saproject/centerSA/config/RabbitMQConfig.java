package com.saproject.centerSA.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_TRANSACTIONS = "transactions_queue";
    public static final String EXCHANGE_TRANSACTIONS = "transactions_exchange";
    public static final String ROUTING_KEY = "transactions_routing_key";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("rabbitmq");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.initialize();
        return rabbitAdmin;
    }


    @Bean
    public Queue transactionsQueue() {
        System.out.println("Creating queue: " + QUEUE_TRANSACTIONS);
        Queue queue = new Queue(QUEUE_TRANSACTIONS, true);
        System.out.println("Queue created: " + queue.getName());
        return queue;
    }

    @Bean
    public DirectExchange transactionsExchange() {
        System.out.println("Creating exchange: " + EXCHANGE_TRANSACTIONS);
        DirectExchange exchange = new DirectExchange(EXCHANGE_TRANSACTIONS);
        System.out.println("Exchange created: " + exchange.getName());
        return exchange;
    }

    @Bean
    public Binding transactionsBinding(Queue transactionsQueue, DirectExchange transactionsExchange) {
        System.out.println("Binding queue to exchange with routing key: " + ROUTING_KEY);
        Binding binding = BindingBuilder.bind(transactionsQueue)
                .to(transactionsExchange)
                .with(ROUTING_KEY);
        System.out.println("Binding created: " + binding);
        return binding;
    }


}
