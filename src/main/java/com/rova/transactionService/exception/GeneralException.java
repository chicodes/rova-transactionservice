package com.rova.transactionService.exception;

import java.io.IOException;

public class GeneralException extends RuntimeException {

    private static final long serialVersionUID = -4967588218802689301L;

    public GeneralException(IOException e) {
        super();
    }

    public GeneralException(String message) {
        super(message);
    }
}
