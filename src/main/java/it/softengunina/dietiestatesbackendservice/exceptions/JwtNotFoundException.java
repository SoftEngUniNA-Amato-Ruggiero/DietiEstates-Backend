package it.softengunina.dietiestatesbackendservice.exceptions;

public class JwtNotFoundException extends RuntimeException {
    public JwtNotFoundException(String message) {
        super(message);
    }
}
