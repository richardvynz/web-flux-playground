package com.richardvinz.webflux_Playground.sec06.config;

import com.richardvinz.webflux_Playground.sec06.exceptions.CustomerNotFoundException;
import com.richardvinz.webflux_Playground.sec06.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Service
public class ApplicationExceptionHandler {

//    public Mono<ServerResponse> handleCustomerNotFoundException(CustomerNotFoundException exception, ServerRequest request){
//        var status = HttpStatus.NOT_FOUND;
//        var problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
//        problem.setType(URI.create("http://example.com/problems/customer-not-found"));
//        problem.setTitle("Customer Not Found");
//        problem.setInstance(URI.create(request.path()));
//        return ServerResponse.status(status).bodyValue(problem);
//    }

//    public Mono<ServerResponse> handleInvalidInputException(InvalidInputException exception, ServerRequest request){
//        var status = HttpStatus.BAD_REQUEST;
//        var problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
//        problem.setType(URI.create("http://example.com/problems/invalid-input-request"));
//        problem.setTitle("Invalid Input Request");
//        problem.setInstance(URI.create(request.path()));
//        return ServerResponse.status(status).bodyValue(problem);
//    }

    private Mono<ServerResponse>handleExceptions(HttpStatus status, Exception ex,
                                                 ServerRequest request,
                                                 Consumer<ProblemDetail> consumer){
        var problem = ProblemDetail.forStatusAndDetail(status,ex.getMessage());
        problem.setInstance(URI.create(request.path()));
        consumer.accept(problem);
        return ServerResponse.status(status).bodyValue(problem);

    }

    public Mono<ServerResponse>handleException(CustomerNotFoundException exception,ServerRequest request) {
        return handleExceptions(HttpStatus.NOT_FOUND,exception,request,problem->{
            problem.setType(URI.create("http://example.com/problems/customer-not-found"));
            problem.setTitle("Customer Not Found");
        });
    }

    public Mono<ServerResponse>handleException(InvalidInputException ex,ServerRequest request){
        return handleExceptions(HttpStatus.BAD_REQUEST,ex,request,problem->{
            problem.setType(URI.create("http://example.com/problems/invalid-input-request"));
            problem.setTitle("Invalid Input Request");
        });
    }
}
