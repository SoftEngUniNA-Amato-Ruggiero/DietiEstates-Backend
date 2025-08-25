package it.softengunina.dietiestatesbackendservice.repository;

import it.softengunina.dietiestatesbackendservice.model.RealEstateAgency;
import it.softengunina.dietiestatesbackendservice.model.users.RealEstateAgent;
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

    @BeforeEach
    void setUp() {
        testAgency = agencyRepository.save(new RealEstateAgency("testIban", "testAgency"));
        existingAgent = agentRepository.save(new RealEstateAgent("existing@email.com", "existingSub", testAgency));
    }

    @Test
    void insertManager() {
        managerRepository.insertManager(existingAgent.getId());
        managerRepository.flush();
        assertTrue(managerRepository.findById(existingAgent.getId()).isPresent());
    }
}