package com.rova.transactionService.controller;

import com.rova.transactionService.dto.RevoResponse;
import com.rova.transactionService.dto.CreateTransactionRequestDto;
import com.rova.transactionService.service.TransactionService;
import com.rova.transactionService.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(Constants.BASE_URL +"/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    @PostMapping("")
    public ResponseEntity<RevoResponse> addTransaction(@RequestBody CreateTransactionRequestDto request){
        RevoResponse resp = transactionService.doTransaction(request);
        return new ResponseEntity<>(resp, resp.getHttpStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RevoResponse> getTransaction(@PathVariable String id) {
        RevoResponse resp = transactionService.getTransaction(id);
        return new ResponseEntity<>(resp, resp.getHttpStatus());
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<RevoResponse> getCustomerTransaction(@PathVariable String id,
                                                               @RequestParam (defaultValue = "0")int pageNo,
                                                               @RequestParam(defaultValue = "10") int pageSize
    ){
        RevoResponse resp = transactionService.getCustomerTransactions(id, pageNo, pageSize);
        return new ResponseEntity<>(resp, resp.getHttpStatus());
    }
}
