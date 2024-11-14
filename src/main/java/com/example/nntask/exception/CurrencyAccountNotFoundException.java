package com.example.nntask.exception;

public class CurrencyAccountNotFoundException extends RuntimeException{
    public CurrencyAccountNotFoundException(String msg) {
        super(msg);
    }
}