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

    public static final String DLX_EXCHANGE = "dlx_exchange";
    public static final String DLQ_TRANSACTIONS = "dlq_transactions_queue";
    public static final String DLQ_ROUTING_KEY = "dlq_routing_key";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin) {
        return event -> {
            rabbitAdmin.declareExchange(transactionsExchange());
            rabbitAdmin.declareExchange(deadLetterExchange());

            rabbitAdmin.declareQueue(transactionQueue());
            rabbitAdmin.declareQueue(deadLetterQueue());

            rabbitAdmin.declareBinding(transactionsBinding(transactionQueue(), transactionsExchange()));
            rabbitAdmin.declareBinding(deadLetterBinding());

            rabbitAdmin.initialize();
        };
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder.durable(QUEUE_TRANSACTIONS)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_TRANSACTIONS).build();
    }

    @Bean
    public DirectExchange transactionsExchange() {
        return new DirectExchange(EXCHANGE_TRANSACTIONS);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Binding transactionsBinding(Queue transactionQueue, DirectExchange transactionsExchange) {
        return BindingBuilder.bind(transactionQueue)
                .to(transactionsExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
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
