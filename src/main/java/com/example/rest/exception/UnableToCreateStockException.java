package com.example.rest.exception;

public class UnableToCreateStockException extends RuntimeException{
    public UnableToCreateStockException(String message){
        super(message);
    }
}
