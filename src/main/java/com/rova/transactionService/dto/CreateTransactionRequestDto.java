package com.rova.transactionService.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rova.transactionService.model.TransactionType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
public class CreateTransactionRequestDto {
    private String customerId;

    @JsonIgnore
    private String initialCredit;
    private String amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}
