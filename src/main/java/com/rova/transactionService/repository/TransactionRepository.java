package com.rova.transactionService.repository;

import com.rova.transactionService.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    List<Transaction> findByCustomerId(String id);

    Page<Transaction> findByCustomerId(String id, Pageable pageable);


    @Query(value = "SELECT * from transaction where customer_id= :customerId ORDER BY id desc LIMIT 1", nativeQuery = true)
    Transaction findCustomerLastTransaction(String customerId);
}
