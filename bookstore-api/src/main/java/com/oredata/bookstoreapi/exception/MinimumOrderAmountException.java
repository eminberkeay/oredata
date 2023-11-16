package com.oredata.bookstoreapi.exception;

public class MinimumOrderAmountException extends RuntimeException {
    public MinimumOrderAmountException(String message) {
        super(message);
    }
}
