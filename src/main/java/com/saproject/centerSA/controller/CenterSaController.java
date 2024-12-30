package com.saproject.centerSA.controller;

import com.saproject.centerSA.client.AccountResponse;
import com.saproject.centerSA.service.CenterSaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/center")
public class CenterSaController {

    @Autowired
    CenterSaService centerSaService;

    @GetMapping("/{id}")
    public AccountResponse findById(@PathVariable Long id) {
        AccountResponse account = centerSaService.findById(id);
        System.out.println("Returned account: " + account);  // Log para verificar os dados
        return account;
    }
}
