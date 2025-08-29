package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.*;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RealEstateAgentRepositoryTest {
    @Autowired
    RealEstateAgencyRepository agencyRepository;
    @Autowired
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @Autowired
    UserRepository<BaseUser> userRepository;
    @Autowired
    RealEstateManagerRepository managerRepository;

    RealEstateAgent testAgent;
    RealEstateManager testManager;
    RealEstateAgency testAgency;
    BaseUser existingUser;

    @BeforeEach
    void setUp() {
        existingUser = userRepository.save(new BaseUser("user@email.com", "userSub"));
        testAgency = agencyRepository.save(new RealEstateAgency("testIban", "testAgency"));
        testAgent = agentRepository.save(new RealEstateAgent("agent@email.com", "agentSub", testAgency));
        testManager = managerRepository.save(new RealEstateManager("manager@email.com", "managerSub", testAgency));
    }

    @Test
    void findByAgency() {
        Page<RealEstateAgent> agents = agentRepository.findByAgency(testAgency, Pageable.unpaged());
        assertAll(
                () -> assertTrue(agents.getContent().contains(testAgent)),
                () -> assertTrue(agents.getContent().contains(testManager)),
                () -> assertEquals(2, agents.getTotalElements())
        );
    }

    @Test
    void insertAgent() {
        agentRepository.insertAgent(existingUser.getId(), testAgency.getId());
        agentRepository.flush();
        assertTrue(agentRepository.findById(existingUser.getId()).isPresent());
    }

    @Test
    void demoteAgent() {
        agentRepository.demoteAgent(testAgent.getId());
        assertAll(
                () -> assertFalse(agentRepository.findById(testAgent.getId()).isPresent()),
                () -> {
                    Optional<BaseUser> demotedUser = userRepository.findById(testAgent.getId());
                    assertTrue(demotedUser.isPresent());
                    assertEquals(testAgent.getUsername(), demotedUser.get().getUsername());
                    assertEquals(testAgent.getCognitoSub(), demotedUser.get().getCognitoSub());
                }
        );
    }

    @Test
    void demoteAgent_WhoIsManager() {
        Long managerId = testManager.getId();
        assertThrows(DataIntegrityViolationException.class, () -> agentRepository.demoteAgent(managerId));
    }
}