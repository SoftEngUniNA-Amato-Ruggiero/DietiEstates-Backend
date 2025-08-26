package it.softengunina.dietiestatesbackend.commands;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public record PromoteAgentToManagerCommand(RealEstateAgent agent) implements PromotionCommand<RealEstateManager> {

    public RealEstateManager execute(PromotionService service) throws PromotionFailedException {
        return service.promoteAgentToManager(agent);
    }
}
