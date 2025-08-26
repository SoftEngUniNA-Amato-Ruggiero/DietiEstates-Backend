package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public interface PromotionToAgentCommand {
    RealEstateAgent execute(PromotionService service) throws PromotionFailedException;
}
