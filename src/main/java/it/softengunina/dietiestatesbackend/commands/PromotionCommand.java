package it.softengunina.dietiestatesbackend.commands;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.services.PromotionService;

public interface PromotionCommand<T extends User> {
    T execute(PromotionService service) throws PromotionFailedException;
}
