package it.softengunina.dietiestatesbackend.strategy;

import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;

public interface UserDemotionStrategy {
    UserWithAgency demoteManagerToAgent(UserWithAgency manager);
    User demoteAgentToUser(UserWithAgency agent);
}
