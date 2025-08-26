package it.softengunina.dietiestatesbackend.commands;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public record PromoteUserToAgentCommand(User user, RealEstateAgency agency) implements PromotionCommand<RealEstateAgent> {

    public RealEstateAgent execute(PromotionService service) throws PromotionFailedException {
        return service.promoteUserToAgent(user, agency);
    }
}
