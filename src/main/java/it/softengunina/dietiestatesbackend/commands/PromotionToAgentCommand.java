package it.softengunina.dietiestatesbackend.commands;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public interface PromotionToAgentCommand {
    RealEstateAgent execute(PromotionService service) throws PromotionFailedException;
}
