package it.softengunina.dietiestatesbackend.commands;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public record PromoteUserToManagerToManagerCommand(User user, RealEstateAgency agency) implements PromotionToManagerCommand {

    public RealEstateManager execute(PromotionService service) throws PromotionFailedException {
        return service.promoteUserToManager(user, agency);
    }
}
