package com.example.rest.exception;

public class UnableToUpdateStockException extends RuntimeException{
    public UnableToUpdateStockException(Integer id){
        super("Unable to update stock with Id:"+id);
    }
}
