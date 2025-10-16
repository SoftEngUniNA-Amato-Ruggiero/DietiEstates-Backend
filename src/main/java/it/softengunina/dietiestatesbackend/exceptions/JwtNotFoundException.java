package it.softengunina.dietiestatesbackend.exceptions;

public class JwtNotFoundException extends TokenServiceException {
    public JwtNotFoundException(String message) {
        super(message);
    }
}
