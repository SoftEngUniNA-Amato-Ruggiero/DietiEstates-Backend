package it.softengunina.dietiestatesbackend.exceptions;

public class TokenServiceException extends RuntimeException {
    public TokenServiceException(String message) {
        super(message);
    }
}
