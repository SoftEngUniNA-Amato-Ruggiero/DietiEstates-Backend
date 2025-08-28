package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
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
class UserDemotionServiceImplTest {
    @Autowired
    RealEstateAgencyRepository agencyRepository;
    @Autowired
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @Autowired
    RealEstateManagerRepository managerRepository;
    @Autowired
    UserRepository<BaseUser> userRepository;
    @Autowired
    UserDemotionServiceImpl demotionService;

    RealEstateAgency agency;
    RealEstateManager manager;
    RealEstateAgent agent;
    BaseUser user;

    @BeforeEach
    void setUp() {
        agency = agencyRepository.saveAndFlush(new RealEstateAgency("agencyIban", "agencyName"));
        manager = managerRepository.saveAndFlush(new RealEstateManager("managerUsername", "managerSub", agency));
        agent = agentRepository.saveAndFlush(new RealEstateAgent("agentUsername", "agentSub", agency));
        user = userRepository.saveAndFlush(new BaseUser("userUsername", "userSub"));
    }

    @Test
    void demoteManagerToAgent() {
        RealEstateAgent demotedAgent = demotionService.demoteManagerToAgent(manager);
        assertAll(
                () -> assertEquals(manager.getUsername(), demotedAgent.getUsername()),
                () -> assertEquals(manager.getCognitoSub(), demotedAgent.getCognitoSub()),
                () -> assertEquals(manager.getAgency(), demotedAgent.getAgency()),
                () -> assertFalse(managerRepository.findById(demotedAgent.getId()).isPresent()),
                () -> assertTrue(agentRepository.findById(demotedAgent.getId()).isPresent()),
                () -> assertTrue(userRepository.findById(demotedAgent.getId()).isPresent())
        );
    }

    @Test
    void demoteAgentToUser() {
        BaseUser demotedUser = demotionService.demoteAgentToUser(agent);
        assertAll(
                () -> assertEquals(agent.getUsername(), demotedUser.getUsername()),
                () -> assertEquals(agent.getCognitoSub(), demotedUser.getCognitoSub()),
                () -> assertNotEquals(agent.getAgency(), demotedUser.getAgency()),
                () -> assertNull(demotedUser.getAgency()),
                () -> assertFalse(managerRepository.findById(demotedUser.getId()).isPresent()),
                () -> assertFalse(agentRepository.findById(demotedUser.getId()).isPresent()),
                () -> assertTrue(userRepository.findById(demotedUser.getId()).isPresent())
        );
    }

    @Test
    void demoteManagerToUser() {
        BaseUser demotedUser = demotionService.demoteManagerToUser(manager);
        assertAll(
                () -> assertEquals(manager.getUsername(), demotedUser.getUsername()),
                () -> assertEquals(manager.getCognitoSub(), demotedUser.getCognitoSub()),
                () -> assertNotEquals(manager.getAgency(), demotedUser.getAgency()),
                () -> assertNull(demotedUser.getAgency()),
                () -> assertFalse(managerRepository.findById(demotedUser.getId()).isPresent()),
                () -> assertFalse(agentRepository.findById(demotedUser.getId()).isPresent()),
                () -> assertTrue(userRepository.findById(demotedUser.getId()).isPresent())
        );
    }
}