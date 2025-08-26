package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public interface PromotionToManagerCommand {
    RealEstateManager execute(PromotionService service) throws PromotionFailedException;
}
