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

    @Autowired
    public TransactionService(AccountFeignClient accountFeignClient, TransactionRepository transactionRepository) {
        this.accountFeignClient = accountFeignClient;
        this.transactionRepository = transactionRepository;
    }

    public AccountDTO deposit(Long accountId, double amount) {
        AccountDTO accountDTO = new AccountDTO(amount);
        AccountDTO updatedAccount = accountFeignClient.deposit(accountDTO, accountId);

        TransactionRecord record = new TransactionRecord();
        record.setType("deposit");
        record.setAccountNumber(String.valueOf(accountId));
        record.setAmount(amount);
        record.setTimestamp(LocalDateTime.now().toString());
        transactionRepository.save(record);

        return updatedAccount;
    }


        public AccountDTO withdraw(Long accountId, double amount) {
        AccountDTO accountDTO = new AccountDTO(amount);
        AccountDTO updatedAccount = accountFeignClient.withdraw(accountDTO, accountId);

        TransactionRecord record = new TransactionRecord();
        record.setType("withdraw");
        record.setAccountNumber(String.valueOf(accountId));
        record.setAmount(amount);
        record.setTimestamp(LocalDateTime.now().toString());
        transactionRepository.save(record);

        return updatedAccount;
    }

    public void transfer(TransactionDTO transactionDTO) {
        accountFeignClient.processTransaction(transactionDTO);

        TransactionRecord record = new TransactionRecord();
        record.setType("transfer");
        record.setOriginAccount(transactionDTO.originAccountNumber());
        record.setDestinationAccount(transactionDTO.destinationAccountNumber());
        record.setAmount(transactionDTO.amount());
        record.setTimestamp(LocalDateTime.now().toString());
        transactionRepository.save(record);
    }
}
