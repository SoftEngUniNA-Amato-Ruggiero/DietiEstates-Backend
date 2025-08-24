package it.softengunina.userservice.repository;

import it.softengunina.userservice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository<User> repository;
    @Autowired
    RealEstateAgencyRepository agencyRepository;

    User testUser;
    RealEstateAgency testAgency;

    @BeforeEach
    void setUp() {
        testAgency = agencyRepository.save(new RealEstateAgency("testIban", "testAgency"));
        testUser = repository.save(new RealEstateAgent("email@test.com", "testsub", testAgency));
    }

    @Test
    void findByUsername() {
        User user = repository.findByUsername("email@test.com").orElse(null);
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(testUser, user)
        );
    }

    @Test
    void findByCognitoSub() {
        User user = repository.findByCognitoSub("testsub").orElse(null);
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