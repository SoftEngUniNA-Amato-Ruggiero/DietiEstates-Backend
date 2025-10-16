package it.softengunina.dietiestatesbackend.exceptions;

public class AuthenticationNotFoundException extends TokenServiceException {
    public AuthenticationNotFoundException(String message) {
        super(message);
    }
}
