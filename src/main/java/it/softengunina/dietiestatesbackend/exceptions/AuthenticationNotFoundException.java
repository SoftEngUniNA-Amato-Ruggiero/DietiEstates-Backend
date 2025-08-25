package it.softengunina.dietiestatesbackend.exceptions;

public class AuthenticationNotFoundException extends RuntimeException {
    public AuthenticationNotFoundException(String message) {
        super(message);
    }
}
