package com.example.rest.exception;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(){
        super("Stocks Not Found!");
    }
    public StockNotFoundException(Integer id){
        super("Stock Not Found with Id:"+id);
    }
}
