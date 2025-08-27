package it.softengunina.dietiestatesbackend.exceptions;

public class ImpossiblePromotionException extends IllegalArgumentException {
    public ImpossiblePromotionException(String message) {
        super(message);
    }
}
