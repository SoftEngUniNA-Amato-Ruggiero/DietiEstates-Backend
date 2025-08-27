package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;

public interface PromotionService {
    UserWithAgency promoteUserToAgent(User user, RealEstateAgency agency);
    UserWithAgency promoteAgentToManager(UserWithAgency agent);
    UserWithAgency promoteUserToManager(User user, RealEstateAgency agency);
}
