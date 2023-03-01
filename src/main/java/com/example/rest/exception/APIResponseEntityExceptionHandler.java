package com.example.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class APIResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
private static final String MESSAGE_INVALID_INPUT = "Invalid input, please verify your input";
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest wr){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(ex.getMessage(),new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Object> handleAllExceptions(StockNotFoundException ex, WebRequest wr){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(ex.getMessage(),new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaginationReqException.class)
    public ResponseEntity<Object> handleAllExceptions(PaginationReqException ex, WebRequest wr){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(ex.getMessage(),new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map( oe -> oe.getDefaultMessage())
                .collect(Collectors.joining(" and "));
        if(errors.isBlank())
            errors = MESSAGE_INVALID_INPUT;
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(errors,new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(MESSAGE_INVALID_INPUT,new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
