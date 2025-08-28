package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RealEstateManagerRepositoryTest {
    @Autowired
    RealEstateAgencyRepository agencyRepository;
    @Autowired
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @Autowired
    RealEstateManagerRepository managerRepository;

    RealEstateAgency testAgency;
    RealEstateAgent existingAgent;
    RealEstateManager existingManager;

    @BeforeEach
    void setUp() {
        testAgency = agencyRepository.save(new RealEstateAgency("testIban", "testAgency"));
        existingAgent = agentRepository.save(new RealEstateAgent("existing@email.com", "existingSub", testAgency));
        existingManager = managerRepository.save(new RealEstateManager("manager@email.com", "managerSub", testAgency));
    }

    @Test
    void insertManager() {
        managerRepository.insertManager(existingAgent.getId());
        managerRepository.flush();
        assertTrue(managerRepository.findById(existingAgent.getId()).isPresent());
    }

    @Test
    void demoteManager() {
        managerRepository.demoteManager(existingManager.getId());
        assertAll(
                () -> assertFalse(managerRepository.findById(existingManager.getId()).isPresent()),
                () -> assertTrue(agentRepository.findById(existingManager.getId()).isPresent())
        );
    }
}