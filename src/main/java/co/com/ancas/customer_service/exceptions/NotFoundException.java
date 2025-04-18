package co.com.ancas.customer_service.exceptions;

public class NotFoundException  extends RuntimeException{

    public  NotFoundException(String message) {
        super(message);
    }
}
