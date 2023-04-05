package com.rova.transactionService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rova.transactionService.TransactionServiceApplication;
import com.rova.transactionService.dto.CreateTransactionRequestDto;
import com.rova.transactionService.model.Transaction;
import com.rova.transactionService.model.TransactionType;
import com.rova.transactionService.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TransactionServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("local")
class TransactionControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionRepository transactionRepository;

    @Test
    void addTransaction() throws Exception {

        CreateTransactionRequestDto transactionRequest =  new CreateTransactionRequestDto();
        transactionRequest.setAmount("345000");
        transactionRequest.setTransactionType(TransactionType.DEPOSIT);
        transactionRequest.setCustomerId(String.valueOf(1));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.respCode", Matchers.is("00")))
                .andExpect(jsonPath("$.respDescription", Matchers.is("SUCCESS")));
    }

    @Test
    void getTransaction() throws Exception{

        Transaction transaction = new Transaction();
        transaction.setId(1l);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(new BigDecimal(500));
        transaction.setBalanceBefore(new BigDecimal(1000));
        transaction.setBalanceAfter(new BigDecimal(1500));
        transaction.setCustomerId(String.valueOf(1));

        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

        Mockito.when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        log.info("ID: {}", transaction.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/"+transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.respCode", Matchers.is("00")))
                .andExpect(jsonPath("$.respDescription", Matchers.is("SUCCESS")));
    }

    @Test
    void getCustomerTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1l);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(new BigDecimal(500));
        transaction.setBalanceBefore(new BigDecimal(1000));
        transaction.setBalanceAfter(new BigDecimal(1500));
        transaction.setCustomerId(String.valueOf(1));

        Integer pageNo = 0; Integer pageSize = 10;
        ArrayList list = new ArrayList();
        list.add(transaction);
        Page<Transaction> page = new PageImpl<>(list);
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Mockito.when(transactionRepository.findByCustomerId(transaction.getCustomerId().toString(), paging)).thenReturn(page);
        log.info("ID: {}", transaction.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/customer/"+transaction.getCustomerId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.respCode", Matchers.is("00")))
                .andExpect(jsonPath("$.respDescription", Matchers.is("SUCCESS")));
    }
}