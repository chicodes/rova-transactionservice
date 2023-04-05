package com.rova.transactionService.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String customerId;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    Date dateCreated;
    BigDecimal amount;
    Date dateUpdated;

    BigDecimal balanceBefore;

    BigDecimal balanceAfter;

}
