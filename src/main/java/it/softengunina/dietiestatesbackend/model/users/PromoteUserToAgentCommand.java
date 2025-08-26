package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public record PromoteUserToAgentCommand(User user, RealEstateAgency agency) implements PromotionToAgentCommand {

    public RealEstateAgent execute(PromotionService service) throws PromotionFailedException {
        return service.promoteUserToAgent(user, agency);
    }
}
