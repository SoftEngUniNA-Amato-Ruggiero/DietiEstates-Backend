package it.softengunina.dietiestatesbackend.strategy;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDemotionStrategyImpl implements UserDemotionStrategy {
    UserRepository<BaseUser> userRepository;
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    RealEstateManagerRepository managerRepository;

    public UserDemotionStrategyImpl(UserRepository<BaseUser> userRepository,
                                    RealEstateAgentRepository<RealEstateAgent> agentRepository,
                                    RealEstateManagerRepository managerRepository) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    @Transactional
    public RealEstateAgent demoteManagerToAgent(@NonNull UserWithAgency manager) {
        managerRepository.demoteManager(manager.getId());
        managerRepository.flush();
        return agentRepository.findById(manager.getId())
                .orElseThrow(() -> new RuntimeException("Agent not found after demotion"));
    }

    @Override
    @Transactional
    public BaseUser demoteAgentToUser(@NonNull UserWithAgency agent) {
        agentRepository.demoteAgent(agent.getId());
        agentRepository.flush();
        return userRepository.findById(agent.getId())
                .orElseThrow(() -> new RuntimeException("User not found after demotion"));
    }
}
