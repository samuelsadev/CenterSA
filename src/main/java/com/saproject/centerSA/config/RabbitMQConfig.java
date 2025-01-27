package com.saproject.centerSA.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_TRANSACTIONS = "transactions_queue";
    public static final String EXCHANGE_TRANSACTIONS = "transactions_exchange";
    public static final String ROUTING_KEY = "transactions_routing_key";

    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicalizarAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate (ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;
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

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
