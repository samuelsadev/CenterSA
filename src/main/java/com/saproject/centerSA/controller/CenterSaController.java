package com.saproject.centerSA.controller;

import com.saproject.centerSA.dto.AccountDTO;
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
    public AccountDTO findById(@PathVariable Long id) {
        AccountDTO account = centerSaService.findById(id);
        System.out.println("Returned account: " + account);
        return account;
    }
}
