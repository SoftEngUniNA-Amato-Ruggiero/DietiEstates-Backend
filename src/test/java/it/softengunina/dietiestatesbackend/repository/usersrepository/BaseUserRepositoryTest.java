package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BaseUserRepositoryTest {
    @Autowired
    BaseUserRepository<BaseUser> repository;

    BaseUser testUser;

    @BeforeEach
    void setUp() {
        testUser = repository.save(new BaseUser("userIban", "usersub"));
    }

    @Test
    void findByUsername() {
        BaseUser user = repository.findByUsername(testUser.getUsername()).orElse(null);
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(testUser, user)
        );
    }

    @Test
    void findByCognitoSub() {
        BaseUser user = repository.findByCognitoSub(testUser.getCognitoSub()).orElse(null);
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(testUser, user)
        );
    }

    @Test
    void existsByUsername() {
        assertTrue(repository.existsByUsername(testUser.getUsername()));
    }

    @Test
    void existsByCognitoSub() {
        assertTrue(repository.existsByCognitoSub(testUser.getCognitoSub()));
    }
}