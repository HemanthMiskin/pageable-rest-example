package com.example.rest.exception;

public class PaginationReqException extends RuntimeException{
    public PaginationReqException(){
        super("Stocks per page and Page number should be valid numbers");
    }
}
