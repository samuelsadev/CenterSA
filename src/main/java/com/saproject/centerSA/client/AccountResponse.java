package com.saproject.centerSA.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("agency")
    private String agency;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("balance")
    private double balance;
}
