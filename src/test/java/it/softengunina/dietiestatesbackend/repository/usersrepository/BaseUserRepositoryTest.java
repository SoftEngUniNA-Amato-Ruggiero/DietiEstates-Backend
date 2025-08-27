package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.*;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.RealEstateAgencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BaseUserRepositoryTest {
    @Autowired
    UserRepository<BaseUser> repository;
    @Autowired
    RealEstateAgencyRepository agencyRepository;

    BaseUser testUser;
    RealEstateAgency testAgency;

    @BeforeEach
    void setUp() {
        testAgency = agencyRepository.save(new RealEstateAgency("testIban", "testAgency"));
        testUser = repository.save(new RealEstateAgent("email@test.com", "testsub", testAgency));
    }

    @Test
    void findByUsername() {
        BaseUser user = repository.findByUsername("email@test.com").orElse(null);
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(testUser, user)
        );
    }

    @Test
    void findByCognitoSub() {
        BaseUser user = repository.findByCognitoSub("testsub").orElse(null);
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(testUser, user)
        );
    }

    @Test
    void existsByUsername() {
        assertTrue(repository.existsByUsername("email@test.com"));
    }

    @Test
    void existsByCognitoSub() {
        assertTrue(repository.existsByCognitoSub("testsub"));
    }
}