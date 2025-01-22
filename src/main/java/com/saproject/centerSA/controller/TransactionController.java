package com.saproject.centerSA.controller;

import com.saproject.centerSA.dto.AccountDTO;
import com.saproject.centerSA.dto.TransactionDTO;
import com.saproject.centerSA.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<AccountDTO> deposit(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = transactionService.deposit(id, accountDTO.balance());
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/withdraw/{id}")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = transactionService.withdraw(id, accountDTO.balance());
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionDTO transactionDTO) {
        transactionService.transfer(transactionDTO);
        return ResponseEntity.ok("Transfer completed successfully.");
    }

}
