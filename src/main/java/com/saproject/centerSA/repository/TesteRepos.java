package com.saproject.centerSA.repository;

import com.saproject.centerSA.model.Testes;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TesteRepos extends MongoRepository <Testes, String> {
}
