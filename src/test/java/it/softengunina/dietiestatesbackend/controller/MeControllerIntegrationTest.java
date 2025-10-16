package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MeControllerIntegrationTest {
    @Autowired
    MeController meController;
    @Autowired
    BaseUserRepository baseUserRepository;

    BaseUser baseUser;
    BaseUser businessUser;

    @BeforeEach
    void setUp() {
        baseUser = baseUserRepository.findById(10L).orElseThrow();
        businessUser = baseUserRepository.findById(20L).orElseThrow();
    }

    @Test
    void getMe_WhenBaseUser() {
        assertNull(meController.getMe(baseUser).getAgency());
    }

    @Test
    void getMe_WhenBusinessUser() {
        assertNotNull(meController.getMe(businessUser).getAgency());
    }
}
