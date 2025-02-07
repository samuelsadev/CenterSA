package com.saproject.centerSA.consumer;

import com.saproject.centerSA.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saproject.centerSA.dto.TransactionDTO;
import com.saproject.centerSA.client.AccountFeignClient;
import com.saproject.centerSA.dto.TransactionRecord;
import com.saproject.centerSA.repository.TransactionRepository;
import java.util.UUID;

@Component
public class RabbitMQConsumer {

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
        try {
            validateTransaction(transactionDTO);
            processTransaction(transactionDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error processing transaction: " + e.getMessage(), e);
        }
    }

    private void validateTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO.originAccountNumber() == null ||
                transactionDTO.destinationAccountNumber() == null ||
                transactionDTO.amount() == null) {
            throw new IllegalArgumentException("mensage invalid!.");
        }
    }

    public void processTransaction(TransactionDTO transactionDTO) {
        String transactionId = UUID.randomUUID().toString();
        TransactionRecord transactionRecord = new TransactionRecord(transactionId, "INITIALIZED", transactionDTO);
        transactionRepository.save(transactionRecord);
        transactionRecord.updateStatus("PROCESSING");
        transactionRepository.save(transactionRecord);

        try {
            accountFeignClient.processTransaction(transactionDTO);
            transactionRecord.updateStatus("COMPLETED");
        } catch (Exception e) {
            transactionRecord.updateStatus("FAILED");
        }
        transactionRepository.save(transactionRecord);
    }
}
