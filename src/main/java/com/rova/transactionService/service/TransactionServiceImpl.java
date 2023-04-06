package com.rova.transactionService.service;

import com.rova.transactionService.dto.RevoResponse;
import com.rova.transactionService.dto.CreateTransactionRequestDto;
import com.rova.transactionService.exception.ResourceNotFoundException;
import com.rova.transactionService.model.Transaction;
import com.rova.transactionService.repository.TransactionRepository;
import com.rova.transactionService.util.ResponseHelper;
import com.rova.transactionService.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import com.rova.transactionService.http.HttpClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.rova.transactionService.util.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ResponseHelper responseHelper;
    private final HttpClient httpClient;
    @Value("${transactionServiceUrl}")
    private String transactionServiceUrl;

    @Override
    public RevoResponse doTransaction(CreateTransactionRequestDto request){
        try {

            Transaction lastTran = getCustomerLastTransactions(request.getCustomerId());
            log.info("Last transaction {}", lastTran);
            //if its a withdrawal transaction and customer has no transaction record throw an exception
            if(Objects.isNull(lastTran) && request.getTransactionType().toString().equals("WITHDRAWAL")){
                throw new ResourceNotFoundException("Customer has no transaction");
            }
            //if its a deposit transaction and customer has no transaction record, do a fresh insert
            else if(Objects.isNull(lastTran) && request.getTransactionType().toString().equals("DEPOSIT")){
                Transaction transaction = new Transaction();
                transaction.setAmount(new BigDecimal(request.getAmount()));
                transaction.setCustomerId(request.getCustomerId());
                transaction.setTransactionType(request.getTransactionType());
                transaction.setBalanceBefore(BigDecimal.valueOf(0.00));
                transaction.setBalanceAfter(new BigDecimal(request.getAmount()));
                transaction.setDateCreated(Utility.getCurrentDate());
                transactionRepository.save(transaction);
                return responseHelper.getResponse(SUCCESS_CODE, SUCCESS, transaction, HttpStatus.CREATED);
            }

            log.info("Amount: {}", request.getAmount());
            log.info("Balance after: {}", lastTran.getBalanceBefore());

            BigDecimal newBalance =  updateCustomerBalance(new BigDecimal(request.getAmount()), lastTran.getBalanceAfter(), String.valueOf(request.getTransactionType()));
            Transaction transaction = new Transaction();
            transaction.setAmount(new BigDecimal(request.getAmount()));
            transaction.setCustomerId(request.getCustomerId());
            transaction.setTransactionType(request.getTransactionType());
            transaction.setBalanceBefore(lastTran.getBalanceAfter());
            transaction.setBalanceAfter(newBalance);
            transaction.setDateCreated(Utility.getCurrentDate());
            transactionRepository.save(transaction);
            return responseHelper.getResponse(SUCCESS_CODE, SUCCESS, transaction, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return responseHelper.getResponse(FAILED_CODE, FAILED, e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    //verify that customer has enough balance before withdrawal
    private Boolean checkCustomerBalance(){

        return false;
    }

    private BigDecimal updateCustomerBalance(BigDecimal amount, BigDecimal balanceAfter, String transactionType){
        BigDecimal newBalance = null;
        if(transactionType.equals("WITHDRAWAL")) {
            newBalance = balanceAfter.subtract(amount);
        }
        else if(transactionType.equals("DEPOSIT")){
            newBalance = balanceAfter.add(amount);
        }
        return newBalance;
    }

    @Override
    public RevoResponse getTransaction(String id){

        try {
            log.info("Getting transaction");
            Optional <Transaction> transaction = transactionRepository.findById(Long.valueOf(id));
            if(!transaction.isPresent())
                //throw new ResourceNotFoundException("Transaction not found");
                throw new ResourceNotFoundException("Transactions not found");
            return responseHelper.getResponse(SUCCESS_CODE, SUCCESS, transaction.get(), HttpStatus.OK);
        }
        catch (Exception e){
            return responseHelper.getResponse(FAILED_CODE, FAILED, e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    private Transaction getCustomerLastTransactions(String id){
        Transaction transaction = transactionRepository.findCustomerLastTransaction(id);
        return transaction;
    }

    public RevoResponse getCustomerTransactions(String id, int pageNo, int pageSize){

        try {
            log.info("Getting customer transaction");
            Page<Transaction> transactions;
            Pageable paging = PageRequest.of(pageNo, pageSize);
            transactions = transactionRepository.findByCustomerId(id, paging);
            if (transactions.getTotalElements() == 0)
                throw new ResourceNotFoundException("No transaction for this user");
            return responseHelper.getResponse(SUCCESS_CODE, SUCCESS, transactions, HttpStatus.OK);
        }
        catch (Exception e){
            return responseHelper.getResponse(FAILED_CODE, FAILED, e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
