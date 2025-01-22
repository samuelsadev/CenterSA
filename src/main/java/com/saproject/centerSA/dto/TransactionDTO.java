package com.saproject.centerSA.dto;

public record TransactionDTO(
        String originAccountNumber,
        String destinationAccountNumber,
        Double amount
) {
}
