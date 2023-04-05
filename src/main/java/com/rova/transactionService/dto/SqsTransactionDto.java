package com.rova.transactionService.dto;

import lombok.Data;

@Data
public class SqsTransactionDto {

    private String amount;
    private String customerId;
}
