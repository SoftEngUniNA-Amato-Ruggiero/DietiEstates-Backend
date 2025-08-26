package it.softengunina.dietiestatesbackend.model.users.commands;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public interface PromotionToManagerCommand {
    RealEstateManager execute(PromotionService service) throws PromotionFailedException;
}
