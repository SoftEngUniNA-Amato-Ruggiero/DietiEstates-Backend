package it.softengunina.dietiestatesbackend.exceptions;

public class JwtNotFoundException extends RuntimeException {
    public JwtNotFoundException(String message) {
        super(message);
    }
}
