package it.softengunina.dietiestatesbackendservice.exceptions;

public class AuthenticationNotFoundException extends RuntimeException {
    public AuthenticationNotFoundException(String message) {
        super(message);
    }
}
