package it.softengunina.dietiestatesbackend.exceptions;

public class UserIsAlreadyAffiliatedWithAgencyException extends RuntimeException {
    public UserIsAlreadyAffiliatedWithAgencyException(String message) {
        super(message);
    }
}
