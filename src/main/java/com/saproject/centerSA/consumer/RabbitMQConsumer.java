package com.saproject.centerSA.consumer;

import com.saproject.centerSA.config.RabbitMQConfig;
import com.saproject.centerSA.client.AccountFeignClient;
import com.saproject.centerSA.dto.TransactionDTO;
import com.saproject.centerSA.dto.TransactionRecord;
import com.saproject.centerSA.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final AccountFeignClient accountFeignClient;
    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    public RabbitMQConsumer(AccountFeignClient accountFeignClient,
                            TransactionRepository transactionRepository,
                            ObjectMapper objectMapper) {
        this.accountFeignClient = accountFeignClient;
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TRANSACTIONS)
    public void receive(@Payload TransactionDTO transactionDTO) {
        String transactionId = UUID.randomUUID().toString();

        try {
            logger.info("[TRANSACTION RECEIVED] ID: {}, Amount: {}", transactionId, transactionDTO.amount());

            validateTransaction(transactionDTO);
            processTransaction(transactionId, transactionDTO);
        } catch (Exception e) {
            logger.error("[TRANSACTION ERROR] ID: {}, Amount: {}, Error: {}", transactionId, transactionDTO.amount(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Error processing transaction: " + e.getMessage(), e);
        }
    }

    private void validateTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO.originAccountNumber() == null ||
                transactionDTO.destinationAccountNumber() == null ||
                transactionDTO.amount() == null ||
                transactionDTO.amount() <= 0) {
            throw new IllegalArgumentException("Invalid transaction: missing or incorrect values.");
        }
    }

    public void processTransaction(String transactionId, TransactionDTO transactionDTO) {
        TransactionRecord transactionRecord = new TransactionRecord(transactionId, "INITIALIZED", transactionDTO);
        transactionRepository.save(transactionRecord);
        logger.info("[TRANSACTION INITIALIZED] ID: {}, Amount: {}", transactionId, transactionDTO.amount());

        transactionRecord.updateStatus("PROCESSING");
        transactionRepository.save(transactionRecord);
        logger.info("[TRANSACTION PROCESSING] ID: {}, Amount: {}", transactionId, transactionDTO.amount());

        try {
            accountFeignClient.processTransaction(transactionDTO);
            transactionRecord.updateStatus("COMPLETED");
            logger.info("[TRANSACTION COMPLETED] ID: {}, Amount: {}", transactionId, transactionDTO.amount());
        } catch (Exception e) {
            transactionRecord.updateStatus("FAILED");
            logger.error("[TRANSACTION FAILED] ID: {}, Amount: {}, Error: {}", transactionId, transactionDTO.amount(), e.getMessage());
            throw new RuntimeException("Failed to process transfer", e);
        }

        transactionRepository.save(transactionRecord);
    }
}
