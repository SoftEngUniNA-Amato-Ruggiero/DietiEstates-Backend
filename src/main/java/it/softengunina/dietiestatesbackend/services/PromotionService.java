package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.User;

public interface PromotionService {
    RealEstateAgent promoteUserToAgent(User user, RealEstateAgency agency);
    RealEstateManager promoteAgentToManager(RealEstateAgent agent);
    RealEstateManager promoteUserToManager(User user, RealEstateAgency agency);
}
