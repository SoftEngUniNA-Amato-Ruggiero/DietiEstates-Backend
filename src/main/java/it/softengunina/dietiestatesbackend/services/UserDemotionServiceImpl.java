package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDemotionServiceImpl implements UserDemotionService {
    UserRepository<BaseUser> userRepository;
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    RealEstateManagerRepository managerRepository;
    UserDemotionServiceImpl self;

    public UserDemotionServiceImpl(UserRepository<BaseUser> userRepository,
                                   RealEstateAgentRepository<RealEstateAgent> agentRepository,
                                   RealEstateManagerRepository managerRepository,
                                   @Lazy UserDemotionServiceImpl self) {
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.self = self;
    }

    @Override
    @Transactional
    public RealEstateAgent demoteManagerToAgent(UserWithAgency manager) {
        managerRepository.demoteManager(manager.getId());
        managerRepository.flush();
        return agentRepository.findById(manager.getId())
                .orElseThrow(() -> new RuntimeException("Agent not found after demotion"));
    }

    @Override
    @Transactional
    public BaseUser demoteAgentToUser(UserWithAgency agent) {
        agentRepository.demoteAgent(agent.getId());
        agentRepository.flush();
        return userRepository.findById(agent.getId())
                .orElseThrow(() -> new RuntimeException("User not found after demotion"));
    }

    @Override
    @Transactional
    public BaseUser demoteManagerToUser(UserWithAgency manager) {
        RealEstateAgent agent = self.demoteManagerToAgent(manager);
        return self.demoteAgentToUser(agent);
    }
}
