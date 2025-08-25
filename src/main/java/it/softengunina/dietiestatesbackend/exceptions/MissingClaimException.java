package it.softengunina.dietiestatesbackend.exceptions;

public class MissingClaimException extends RuntimeException {
    public MissingClaimException(String message) {
        super(message);
    }
}
