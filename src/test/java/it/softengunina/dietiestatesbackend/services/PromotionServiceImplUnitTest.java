package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.PromotionFailedException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PromotionServiceImplUnitTest {
    PromotionServiceImpl promotionService;

    @Mock
    PromotionServiceImpl self;
    @Mock
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @Mock
    RealEstateManagerRepository managerRepository;

    @Mock
    BaseUser user;
    @Mock
    RealEstateAgent agent;
    @Mock
    RealEstateAgency agency;

    Long userId = 1L;
    Long agentId = 2L;

    AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        promotionService = new PromotionServiceImpl(self, agentRepository, managerRepository);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(agent.getId()).thenReturn(agentId);

    }

    /**
     * These tests are meant to cover the branches in PromotionServiceImpl not covered by the integration tests
     */

    @Test
    void promoteUserToAgent_notFound() {
        Mockito.when(agentRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(PromotionFailedException.class, () -> promotionService.promoteUserToAgent(user, agency));
    }

    @Test
    void promoteAgentToManager_notFound() {
        Mockito.when(managerRepository.findById(agent.getId())).thenReturn(Optional.empty());
        assertThrows(PromotionFailedException.class, () -> promotionService.promoteAgentToManager(agent));
    }
}
