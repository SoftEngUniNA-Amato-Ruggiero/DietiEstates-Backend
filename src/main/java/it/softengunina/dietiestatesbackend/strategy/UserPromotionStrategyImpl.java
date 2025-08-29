package it.softengunina.dietiestatesbackend.strategy;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.*;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPromotionStrategyImpl implements UserPromotionStrategy {
    private final RealEstateAgentRepository<RealEstateAgent> agentRepository;
    private final RealEstateManagerRepository managerRepository;

    public UserPromotionStrategyImpl(RealEstateAgentRepository<RealEstateAgent> agentRepository,
                                     RealEstateManagerRepository managerRepository) {
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    @Transactional
    public RealEstateAgent promoteUserToAgent(@NonNull User user, @NonNull RealEstateAgency agency) {
        agentRepository.insertAgent(user.getId(), agency.getId());
        agentRepository.flush();
        return agentRepository.findById(user.getId())
                .orElseThrow(() -> new PromotionFailedException("Agent was not saved to the database"));
    }

    @Override
    @Transactional
    public RealEstateManager promoteAgentToManager(@NonNull UserWithAgency agent) {
        managerRepository.insertManager(agent.getId());
        managerRepository.flush();
        return managerRepository.findById(agent.getId())
                .orElseThrow(() -> new PromotionFailedException("Manager was not saved to the database"));
    }
}
