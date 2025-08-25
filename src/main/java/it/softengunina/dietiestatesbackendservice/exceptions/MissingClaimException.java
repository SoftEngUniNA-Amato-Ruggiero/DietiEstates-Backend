package it.softengunina.dietiestatesbackendservice.exceptions;

public class MissingClaimException extends RuntimeException {
    public MissingClaimException(String message) {
        super(message);
    }
}
