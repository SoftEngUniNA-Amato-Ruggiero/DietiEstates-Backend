package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.*;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPromotionServiceImpl implements UserPromotionService {
    private final UserPromotionServiceImpl self;
    private final RealEstateAgentRepository<RealEstateAgent> agentRepository;
    private final RealEstateManagerRepository managerRepository;

    public UserPromotionServiceImpl(@Lazy UserPromotionServiceImpl self,
                                    RealEstateAgentRepository<RealEstateAgent> agentRepository,
                                    RealEstateManagerRepository managerRepository) {
        this.self = self;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    @Transactional
    public RealEstateAgent promoteUserToAgent(User user, RealEstateAgency agency) {
        agentRepository.insertAgent(user.getId(), agency.getId());
        agentRepository.flush();
        return agentRepository.findById(user.getId())
                .orElseThrow(() -> new PromotionFailedException("Agent was not saved to the database"));
    }

    @Override
    @Transactional
    public RealEstateManager promoteAgentToManager(UserWithAgency agent) {
        managerRepository.insertManager(agent.getId());
        managerRepository.flush();
        return managerRepository.findById(agent.getId())
                .orElseThrow(() -> new PromotionFailedException("Manager was not saved to the database"));
    }

    @Override
    @Transactional
    public RealEstateManager promoteUserToManager(User user, RealEstateAgency agency) {
        RealEstateAgent agent = self.promoteUserToAgent(user, agency);
        return self.promoteAgentToManager(agent);
    }
}
