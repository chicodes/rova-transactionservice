package com.rova.transactionService.exception;

import java.io.IOException;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4967588218802689301L;

    public ResourceNotFoundException(IOException e) {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
