package it.softengunina.userservice.exceptions;

public class MissingClaimException extends RuntimeException {
    public MissingClaimException(String message) {
        super(message);
    }
}
