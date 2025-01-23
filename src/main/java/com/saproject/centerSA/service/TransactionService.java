package com.saproject.centerSA.service;

import com.saproject.centerSA.client.AccountFeignClient;
import com.saproject.centerSA.dto.AccountDTO;
import com.saproject.centerSA.dto.TransactionDTO;
import com.saproject.centerSA.dto.TransactionRecord;
import com.saproject.centerSA.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final AccountFeignClient accountFeignClient;
    private final TransactionRepository transactionRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @Autowired
    public TransactionService(AccountFeignClient accountFeignClient,
                              TransactionRepository transactionRepository,
                              RabbitMQProducer rabbitMQProducer) {
        this.accountFeignClient = accountFeignClient;
        this.transactionRepository = transactionRepository;
        this.rabbitMQProducer = rabbitMQProducer;
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

            rabbitMQProducer.sendMessage("Deposit successful for Account ID: " + accountId + ", Amount: " + amount);

            return updatedAccount;
        } catch (Exception e) {
            rabbitMQProducer.sendMessage("Error during deposit for Account ID: " + accountId + " - " + e.getMessage());
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

            rabbitMQProducer.sendMessage("Withdraw successful for Account ID: " + accountId + ", Amount: " + amount);

            return updatedAccount;
        } catch (Exception e) {
            rabbitMQProducer.sendMessage("Error during withdraw for Account ID: " + accountId + " - " + e.getMessage());
            throw e;
        }
    }

    public void transfer(TransactionDTO transactionDTO) {
        try {
            accountFeignClient.processTransaction(transactionDTO);

            TransactionRecord record = new TransactionRecord();
            record.setType("transfer");
            record.setOriginAccount(transactionDTO.originAccountNumber());
            record.setDestinationAccount(transactionDTO.destinationAccountNumber());
            record.setAmount(transactionDTO.amount());
            record.setTimestamp(LocalDateTime.now().toString());
            transactionRepository.save(record);

            rabbitMQProducer.sendMessage("Transfer successful: " +
                    "From " + transactionDTO.originAccountNumber() +
                    " To " + transactionDTO.destinationAccountNumber() +
                    ", Amount: " + transactionDTO.amount());
        } catch (Exception e) {
            rabbitMQProducer.sendMessage("Error during transfer: " + e.getMessage());
            throw e;
        }
    }
}

