package it.softengunina.userservice.services;

import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateAgentRepository;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromotionService {
    private final RealEstateAgentRepository<RealEstateAgent> agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final PromotionService self;

    PromotionService(RealEstateAgentRepository<RealEstateAgent> agentRepository,
                     RealEstateManagerRepository managerRepository,
                     @Lazy PromotionService self) {
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.self = self;
    }

    @Transactional
    public RealEstateAgent promoteUserToAgent(User user, RealEstateAgency agency) {
        agentRepository.insertAgent(user.getId(), agency.getId());
        agentRepository.flush();
        return agentRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Agent was not saved to the database"));
    }

    @Transactional
    public RealEstateManager promoteAgentToManager(RealEstateAgent agent) {
        managerRepository.insertManager(agent.getId());
        managerRepository.flush();
        return managerRepository.findById(agent.getId())
                .orElseThrow(() -> new RuntimeException("Manager was not saved to the database"));
    }

    @Transactional
    public RealEstateManager promoteUserToManager(User user, RealEstateAgency agency) {
        RealEstateAgent agent = self.promoteUserToAgent(user, agency);
        return self.promoteAgentToManager(agent);
    }
}
