package com.example.rest.exception;

public class UnableToDeleteStockException extends RuntimeException{
    public UnableToDeleteStockException(Integer id){
        super("Unable to Delete stock with Id:"+id);
    }
}
