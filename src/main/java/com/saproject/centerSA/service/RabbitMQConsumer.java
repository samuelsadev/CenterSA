package com.saproject.centerSA.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saproject.centerSA.config.RabbitMQConfig;
import com.saproject.centerSA.dto.TransactionDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public RabbitMQConsumer(TransactionService transactionService, ObjectMapper objectMapper) {
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TRANSACTIONS)
    public void receiveMessage(String message) {
        try {
            if (message.startsWith("deposit:")) {
                String[] parts = message.split(":");
                Long accountId = Long.parseLong(parts[1]);
                double amount = Double.parseDouble(parts[2]);
                transactionService.deposit(accountId, amount);
                System.out.println("Processed deposit: Account ID " + accountId + ", Amount " + amount);

            } else if (message.startsWith("withdraw:")) {
                String[] parts = message.split(":");
                Long accountId = Long.parseLong(parts[1]);
                double amount = Double.parseDouble(parts[2]);
                transactionService.withdraw(accountId, amount);
                System.out.println("Processed withdraw: Account ID " + accountId + ", Amount " + amount);

            } else if (message.startsWith("transfer:")) {
                String[] parts = message.split(":");
                String originAccount = parts[1];
                String destinationAccount = parts[2];
                double amount = Double.parseDouble(parts[3]);

                TransactionDTO transactionDTO = new TransactionDTO(originAccount, destinationAccount, amount);
                transactionService.transfer(transactionDTO);
                System.out.println("Processed transfer: From " + originAccount + " to " + destinationAccount + ", Amount " + amount);

            } else {
                System.err.println("Unknown operation: " + message);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + message + " - " + e.getMessage());
        }
    }
}
