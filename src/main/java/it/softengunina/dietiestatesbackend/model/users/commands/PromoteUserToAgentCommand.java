package it.softengunina.dietiestatesbackend.model.users.commands;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public record PromoteUserToAgentCommand(User user, RealEstateAgency agency) implements PromotionToAgentCommand {

    public RealEstateAgent execute(PromotionService service) throws PromotionFailedException {
        return service.promoteUserToAgent(user, agency);
    }
}
