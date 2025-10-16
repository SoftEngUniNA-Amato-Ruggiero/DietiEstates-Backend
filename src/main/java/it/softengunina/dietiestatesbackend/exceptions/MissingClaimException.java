package it.softengunina.dietiestatesbackend.exceptions;

public class MissingClaimException extends TokenServiceException {
    public MissingClaimException(String message) {
        super(message);
    }
}
