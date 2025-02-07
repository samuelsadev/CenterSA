package com.saproject.centerSA.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin) {
        return event -> {
            rabbitAdmin.declareQueue(transactionQueue());
            rabbitAdmin.declareExchange(transactionsExchange());
            rabbitAdmin.declareBinding(transactionsBinding(transactionQueue(), transactionsExchange()));
            rabbitAdmin.initialize();
        };
    }


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(QUEUE_TRANSACTIONS, true, false, false);
    }

    @Bean
    public DirectExchange transactionsExchange() {
        return new DirectExchange(EXCHANGE_TRANSACTIONS);
    }

    @Bean
    public Binding transactionsBinding(Queue transactionQueue, DirectExchange transactionsExchange) {
        return BindingBuilder.bind(transactionQueue)
                .to(transactionsExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setDefaultRequeueRejected(false);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(5);
        factory.setPrefetchCount(10);
        factory.setRecoveryInterval(5000L);
        return factory;
    }
}
