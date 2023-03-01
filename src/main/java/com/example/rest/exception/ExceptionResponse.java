package com.example.rest.exception;

import java.sql.Timestamp;

public class ExceptionResponse {
    private String message;
    private Timestamp timestamp;

    public ExceptionResponse( String message, Timestamp timestamp){
        super();
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
