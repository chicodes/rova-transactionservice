package com.rova.transactionService.controller;

import com.rova.transactionService.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.BASE_URL)
public class HealthCheckController {

    private HttpStatus httpStatus;

    @GetMapping("healthCheck")
    public ResponseEntity<?> healthCheck(){
        return new ResponseEntity<>(200, HttpStatus.OK);

    }
}
