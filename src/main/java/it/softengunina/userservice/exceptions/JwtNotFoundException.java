package it.softengunina.userservice.exceptions;

public class JwtNotFoundException extends RuntimeException {
    public JwtNotFoundException(String message) {
        super(message);
    }
}
