package com.saproject.centerSA.service;

import com.saproject.centerSA.client.AccountFeignClient;
import com.saproject.centerSA.consumer.RabbitMQConsumer;
import com.saproject.centerSA.dto.AccountDTO;
import com.saproject.centerSA.dto.TransactionDTO;
import com.saproject.centerSA.dto.TransactionRecord;
import com.saproject.centerSA.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final AccountFeignClient accountFeignClient;
    private final TransactionRepository transactionRepository;
    private final RabbitMQConsumer rabbitMQConsumer;

    @Autowired
    public TransactionService(AccountFeignClient accountFeignClient,
                              TransactionRepository transactionRepository,
                              RabbitMQConsumer rabbitMQConsumer) {
        this.accountFeignClient = accountFeignClient;
        this.transactionRepository = transactionRepository;
        this.rabbitMQConsumer = rabbitMQConsumer;
    }

    public AccountDTO deposit(Long accountId, double amount) {
        try {
            AccountDTO accountDTO = new AccountDTO(amount);
            AccountDTO updatedAccount = accountFeignClient.deposit(accountDTO, accountId);

            TransactionRecord record = new TransactionRecord();
            record.setType("deposit");
            record.setAccountNumber(String.valueOf(accountId));
            record.setAmount(amount);
            record.setTimestamp(LocalDateTime.now().toString());
            transactionRepository.save(record);

            return updatedAccount;
        } catch (Exception e) {
            throw e;
        }
    }

    public AccountDTO withdraw(Long accountId, double amount) {
        try {
            AccountDTO accountDTO = new AccountDTO(amount);
            AccountDTO updatedAccount = accountFeignClient.withdraw(accountDTO, accountId);

            TransactionRecord record = new TransactionRecord();
            record.setType("withdraw");
            record.setAccountNumber(String.valueOf(accountId));
            record.setAmount(amount);
            record.setTimestamp(LocalDateTime.now().toString());
            transactionRepository.save(record);

            return updatedAccount;
        } catch (Exception e) {
            throw e;
        }
    }

    public void transfer(TransactionDTO transactionDTO) {
        String transactionId = UUID.randomUUID().toString();
        rabbitMQConsumer.processTransaction(transactionId, transactionDTO);
    }
}
