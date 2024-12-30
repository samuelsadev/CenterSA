package com.saproject.centerSA.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Testes {
    @Id
    private String id;
    private String name;
    private int idade;

}
