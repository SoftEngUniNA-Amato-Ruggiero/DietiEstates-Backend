package it.softengunina.userservice.services;

import it.softengunina.userservice.model.RealEstateAgency;
import it.softengunina.userservice.model.RealEstateAgent;
import it.softengunina.userservice.model.RealEstateManager;
import it.softengunina.userservice.model.User;
import it.softengunina.userservice.repository.RealEstateAgencyRepository;
import it.softengunina.userservice.repository.RealEstateAgentRepository;
import it.softengunina.userservice.repository.RealEstateManagerRepository;
import it.softengunina.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class PromotionServiceTest {
    @Autowired
    RealEstateAgencyRepository agencyRepository;
    @Autowired
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @Autowired
    UserRepository<User> userRepository;
    @Autowired
    PromotionService promotionService;

    RealEstateAgency agency;
    RealEstateManager manager;
    RealEstateAgent agent;
    User user;
    @Autowired
    private RealEstateManagerRepository realEstateManagerRepository;

    @BeforeEach
    void setUp() {
        agency = agencyRepository.saveAndFlush(new RealEstateAgency("agencyIban", "agencyName"));
        manager = realEstateManagerRepository.saveAndFlush(new RealEstateManager("managerUsername", "managerSub", agency));
        agent = agentRepository.saveAndFlush(new RealEstateAgent("agentUsername", "agentSub", agency));
        user = userRepository.saveAndFlush(new User("userUsername", "userSub"));
    }

    @Test
    void promoteToAgent() {
        RealEstateAgent promotedAgent = promotionService.promoteToAgent(user, agency);
        assertAll(
            () -> assertNotNull(promotedAgent),
            () -> assertEquals(user.getId(), promotedAgent.getId()),
            () -> assertEquals(agency.getId(), promotedAgent.getAgency().getId())
        );
    }

    @Test
    void promoteToAgent_UserIsAgent() {
        assertThrows(IllegalArgumentException.class, () ->
                promotionService.promoteToAgent(agent, agency)
        );
    }

    @Test
    void promoteToManager() {
        RealEstateManager promotedManager = promotionService.promoteToManager(user, agency);
        assertAll(
                () -> assertNotNull(promotedManager),
                () -> assertEquals(user.getId(), promotedManager.getId()),
                () -> assertEquals(agency.getId(), promotedManager.getAgency().getId())
        );
    }

    @Test
    void promoteToManager_UserIsAgentFromSameAgency() {
        RealEstateManager promotedManager = promotionService.promoteToManager(agent, agency);
        assertAll(
                () -> assertNotNull(promotedManager),
                () -> assertEquals(agent.getId(), promotedManager.getId()),
                () -> assertEquals(agency.getId(), promotedManager.getAgency().getId())
        );
    }

    @Test
    void promoteToManager_UserIsAgentFromAnotherAgency() {
        RealEstateAgency anotherAgency = agencyRepository.saveAndFlush(new RealEstateAgency("anotherIban", "anotherName"));
        RealEstateAgent anotherAgent = agentRepository.saveAndFlush(new RealEstateAgent("anotherUsername", "anotherSub", anotherAgency));

        assertThrows(IllegalArgumentException.class, () ->
                promotionService.promoteToManager(anotherAgent, agency)
        );
    }

    @Test
    void promoteToManager_UserIsManagerFromSameAgency() {
        assertThrows(IllegalArgumentException.class, () ->
            promotionService.promoteToManager(manager, agency)
        );
    }

    @Test
    void promoteToManager_UserIsManagerFromAnotherAgency() {
        RealEstateAgency anotherAgency = agencyRepository.saveAndFlush(new RealEstateAgency("anotherIban", "anotherName"));
        RealEstateManager anotherManager = realEstateManagerRepository.saveAndFlush(new RealEstateManager("anotherManagerUsername", "anotherManagerSub", anotherAgency));

        assertThrows(IllegalArgumentException.class, () ->
            promotionService.promoteToManager(anotherManager, agency)
        );
    }

    @Test
    void promoteUserToAgent() {
        RealEstateAgent promotedAgent = promotionService.promoteUserToAgent(user, agency);
        assertAll(
            () -> assertNotNull(promotedAgent),
            () -> assertEquals(user.getId(), promotedAgent.getId()),
            () -> assertEquals(agency.getId(), promotedAgent.getAgency().getId())
        );
    }

    @Test
    void promoteAgentToManager() {
        RealEstateManager promotedManager = promotionService.promoteAgentToManager(agent);
        assertAll(
                () -> assertNotNull(promotedManager),
                () -> assertEquals(agent.getId(), promotedManager.getId())
        );
    }

    @Test
    void promoteUserToManager() {
        RealEstateManager promotedManager = promotionService.promoteUserToManager(user, agency);
        assertAll(
                () -> assertNotNull(promotedManager),
                () -> assertEquals(user.getId(), promotedManager.getId()),
                () -> assertEquals(agency.getId(), promotedManager.getAgency().getId())
        );
    }

}