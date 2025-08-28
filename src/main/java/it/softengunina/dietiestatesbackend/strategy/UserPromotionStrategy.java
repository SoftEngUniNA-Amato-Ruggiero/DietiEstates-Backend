package it.softengunina.dietiestatesbackend.strategy;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;

public interface UserPromotionStrategy {
    UserWithAgency promoteUserToAgent(User user, RealEstateAgency agency);
    UserWithAgency promoteAgentToManager(UserWithAgency agent);
}
