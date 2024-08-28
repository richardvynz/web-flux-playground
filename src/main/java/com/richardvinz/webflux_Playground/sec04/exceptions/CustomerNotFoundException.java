package com.richardvinz.webflux_Playground.sec04.exceptions;

public class CustomerNotFoundException extends RuntimeException{
    private static final String MESSAGE = "Customer [id = %d] is not found";

    public CustomerNotFoundException(Integer id){
        super(MESSAGE.formatted(id));
    }
}
