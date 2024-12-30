package com.saproject.centerSA.service;

import com.saproject.centerSA.client.AccountBankSAClient;
import com.saproject.centerSA.client.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CenterSaService {

    @Autowired
    AccountBankSAClient accountBankSAClient;

    public AccountResponse findById(Long id) {
        return accountBankSAClient.findById(id);
    }
}
