package co.com.ancas.customer_service.advice;

import co.com.ancas.customer_service.exceptions.BadRequestException;
import co.com.ancas.customer_service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@ControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail customerNotFoundException(NotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setType(URI.create("http://ancas.co/errors/not-found"));
        problemDetail.setTitle("Customer not found");
        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail customInvalidInputException(BadRequestException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("http://ancas.co/errors/invalid-input"));
        problemDetail.setTitle("Invalid input");
        return problemDetail;
    }
}
