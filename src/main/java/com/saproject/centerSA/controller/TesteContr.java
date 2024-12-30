package com.saproject.centerSA.controller;

import com.saproject.centerSA.model.Testes;
import com.saproject.centerSA.service.TesteSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testes")
public class TesteContr {

    @Autowired
    TesteSer testeSer;

    @PostMapping
    public void salvar(@RequestBody Testes testes){
        testeSer.savar(testes);
    }
}
