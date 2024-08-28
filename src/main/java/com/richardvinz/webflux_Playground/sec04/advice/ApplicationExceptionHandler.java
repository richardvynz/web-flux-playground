package com.richardvinz.webflux_Playground.sec04.advice;

import com.richardvinz.webflux_Playground.sec04.exceptions.CustomerNotFoundException;
import com.richardvinz.webflux_Playground.sec04.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException exception){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setType(URI.create("http://example.com/problems/customer-not-found"));
        problem.setTitle("Customer Not Found");
        return problem;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail handleInvalidInputException(InvalidInputException exception){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setType(URI.create("http://example.com/problems/invalid-input-request"));
        problem.setTitle("Invalid Input Request");
        return problem;
    }
}
