package com.saproject.centerSA.client;

import com.saproject.centerSA.dto.AccountDTO;
import com.saproject.centerSA.dto.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "account-service", url = "http://localhost:8083/api/account")
public interface AccountFeignClient {

    @PostMapping("/deposit/{id}")
    AccountDTO deposit(@RequestBody AccountDTO accountDTO, @PathVariable Long id);

    @PostMapping("/withdraw/{id}")
    AccountDTO withdraw(@RequestBody AccountDTO accountDTO, @PathVariable Long id);

    @PostMapping("/transaction")
    void processTransaction(@RequestBody TransactionDTO transactionDTO);
}
