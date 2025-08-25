package it.softengunina.userservice.repository;

import it.softengunina.userservice.model.*;
import it.softengunina.userservice.model.users.RealEstateAgent;
import it.softengunina.userservice.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RealEstateAgentRepositoryTest {
    @Autowired
    RealEstateAgencyRepository agencyRepository;
    @Autowired
    RealEstateAgentRepository<RealEstateAgent> agentRepository;
    @Autowired
    UserRepository<User> userRepository;

    RealEstateAgent testAgent;
    RealEstateAgency testAgency;
    User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = userRepository.save(new User("existing@email.com", "existingSub"));
        testAgency = agencyRepository.save(new RealEstateAgency("testIban", "testAgency"));
        testAgent = agentRepository.save(new RealEstateAgent("email@test.com", "testSub", testAgency));
    }

    @Test
    void findByAgency() {
        Page<RealEstateAgent> agents = agentRepository.findByAgency(testAgency, Pageable.unpaged());
        assertAll(
                () -> assertTrue(agents.getContent().contains(testAgent)),
                () -> assertEquals(1, agents.getTotalElements())
        );
    }

    @Test
    void insertAgent() {
        agentRepository.insertAgent(existingUser.getId(), testAgency.getId());
        agentRepository.flush();
        assertTrue(agentRepository.findById(existingUser.getId()).isPresent());
    }
}