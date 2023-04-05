package com.rova.transactionService.service;

import com.rova.transactionService.dto.RevoResponse;
import com.rova.transactionService.dto.CreateTransactionRequestDto;

public interface TransactionService {

    RevoResponse doTransaction(CreateTransactionRequestDto request);

    RevoResponse getTransaction(String id);



    RevoResponse getCustomerTransactions(String id, int pageNo, int pageSize);
}
