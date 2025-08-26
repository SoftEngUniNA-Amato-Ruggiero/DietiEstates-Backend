package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public record PromoteAgentToManagerToManagerCommand(RealEstateAgent agent) implements PromotionToManagerCommand {

    public RealEstateManager execute(PromotionService service) throws PromotionFailedException {
        return service.promoteAgentToManager(agent);
    }
}
