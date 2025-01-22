package com.saproject.centerSA.client;

import com.saproject.centerSA.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accountBankClient", url = "http://localhost:8080/api/account")
public interface AccountBankSAClient {

    @GetMapping("/{id}")
    public AccountDTO findById(@PathVariable Long id);
}
