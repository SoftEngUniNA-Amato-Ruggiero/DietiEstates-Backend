package it.softengunina.userservice.services;

import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.users.RealEstateAgent;
import it.softengunina.userservice.model.users.RealEstateManager;
import it.softengunina.userservice.model.users.User;
import it.softengunina.userservice.repository.RealEstateAgentRepository;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPromotionService {
    private final RealEstateAgentRepository<RealEstateAgent> agentRepository;
    private final RealEstateManagerRepository managerRepository;
    private final UserPromotionService self;

    UserPromotionService(RealEstateAgentRepository<RealEstateAgent> agentRepository,
                         RealEstateManagerRepository managerRepository,
                         @Lazy UserPromotionService self) {
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.self = self;
    }

    @Transactional
    public RealEstateAgent promoteToAgent(User user, RealEstateAgency agency) {
        if (user instanceof RealEstateAgent) {
            throw new IllegalArgumentException("User is already an agent");
        }
        return self.promoteUserToAgent(user, agency);
    }

    @Transactional
    public RealEstateManager promoteToManager(User user, RealEstateAgency agency) {
        switch (user) {
            case RealEstateManager u -> {
                if (u.getAgency().equals(agency)) {
                    throw new IllegalArgumentException("User is already a manager of this agency");
                } else {
                    throw new IllegalArgumentException("User belongs to another agency");
                }
            }
            case RealEstateAgent u -> {
                if (u.getAgency().equals(agency)) {
                    return self.promoteAgentToManager(u);
                } else {
                    throw new IllegalArgumentException("User belongs to another agency");
                }
            }
            default -> {
                return self.promoteUserToManager(user, agency);
            }
        }
    }

    @Transactional
    RealEstateAgent promoteUserToAgent(User user, RealEstateAgency agency) {
        agentRepository.insertAgent(user.getId(), agency.getId());
        agentRepository.flush();
        return agentRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Agent was not saved to the database"));
    }

    @Transactional
    RealEstateManager promoteAgentToManager(RealEstateAgent agent) {
        managerRepository.insertManager(agent.getId());
        managerRepository.flush();
        return managerRepository.findById(agent.getId())
                .orElseThrow(() -> new RuntimeException("Manager was not saved to the database"));
    }

    @Transactional
    RealEstateManager promoteUserToManager(User user, RealEstateAgency agency) {
        RealEstateAgent agent = self.promoteUserToAgent(user, agency);
        return self.promoteAgentToManager(agent);
    }
}
