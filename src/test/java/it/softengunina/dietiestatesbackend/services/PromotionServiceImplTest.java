package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateAgentRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.UserRepository;
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
class PromotionServiceImplTest {
    @Autowired
    RealEstateAgencyRepository agencyRepository;
    @Autowired
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @Autowired
    RealEstateManagerRepository realEstateManagerRepository;
    @Autowired
    UserRepository<User> userRepository;
    @Autowired
    PromotionServiceImpl promotionService;

    RealEstateAgency agency;
    RealEstateManager manager;
    RealEstateAgent agent;
    User user;

    RealEstateAgency agencyNotInDb;
    RealEstateAgent agentNotInDb;
    User userNotInDb;

    @BeforeEach
    void setUp() {
        agency = agencyRepository.saveAndFlush(new RealEstateAgency("agencyIban", "agencyName"));
        manager = realEstateManagerRepository.saveAndFlush(new RealEstateManager("managerUsername", "managerSub", agency));
        agent = agentRepository.saveAndFlush(new RealEstateAgent("agentUsername", "agentSub", agency));
        user = userRepository.saveAndFlush(new User("userUsername", "userSub"));

        agencyNotInDb = new RealEstateAgency("notInDbIban", "notInDbAgency");
        agentNotInDb = new RealEstateAgent("notInDbAgent", "notInDbAgentSub", agency);
        userNotInDb = new User("notInDbUser", "notInDbUserSub");
    }

    /** Black box R-WECT tests for promoteUserToAgent:
     * - user is a regular user, agency is valid -> success
     * - user is already an agent -> exception
     * - user is already a manager -> exception
     * - user is not in the database -> exception
     * - user is null -> exception
     * - agency is null -> exception
     * - agency is not in the database -> exception
     */

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
    void promoteUserToAgent_userIsExistingAgent() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteUserToAgent(agent, agency));
    }

    @Test
    void promoteUserToAgent_userIsExistingManager() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteUserToAgent(manager, agency));
    }

    @Test
    void promoteUserToAgent_userIsNotInDb() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteUserToAgent(userNotInDb, agency));
    }

    @Test
    void promoteUserToAgent_userIsNull() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteUserToAgent(null, agency));
    }

    @Test
    void promoteUserToAgent_agencyIsNotInDb() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteUserToAgent(user, agencyNotInDb));
    }

    @Test
    void promoteUserToAgent_agencyIsNull() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteUserToAgent(user, null));
    }

    /** Black box R-WECT tests for promoteAgentToManager:
     * - agent is a regular agent, agency is valid -> success
     * - agent is already a manager -> exception
     * - agent is not in the database -> exception
     * - agent is null -> exception
     */

    @Test
    void promoteAgentToManager() {
        RealEstateManager promotedManager = promotionService.promoteAgentToManager(agent);
        assertAll(
                () -> assertNotNull(promotedManager),
                () -> assertEquals(agent.getId(), promotedManager.getId())
        );
    }

    @Test
    void promoteAgentToManager_agentIsExistingManager() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteAgentToManager(manager));
    }

    @Test
    void promoteAgentToManager_agentIsNotInDb() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteAgentToManager(agentNotInDb));
    }

    @Test
    void promoteAgentToManager_agentIsNull() {
        assertThrows(RuntimeException.class, () -> promotionService.promoteAgentToManager(null));
    }

    /** No need to test promoteUserToManager separately, as it is composed of the other two methods.
     * Just a simple integration test to check that everything works as expected.
     */

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