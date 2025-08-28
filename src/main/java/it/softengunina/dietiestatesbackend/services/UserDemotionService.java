package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;

public interface UserDemotionService {
    UserWithAgency demoteManagerToAgent(UserWithAgency manager);
    User demoteAgentToUser(UserWithAgency agent);
    User demoteManagerToUser(UserWithAgency manager);
}
