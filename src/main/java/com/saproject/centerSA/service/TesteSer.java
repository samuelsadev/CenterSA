package com.saproject.centerSA.service;

import com.saproject.centerSA.model.Testes;
import com.saproject.centerSA.repository.TesteRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TesteSer {

    @Autowired
    TesteRepos testeRepos;

    public void savar(Testes testes){
        testeRepos.save(testes);
    }
}
