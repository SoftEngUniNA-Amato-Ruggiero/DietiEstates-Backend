package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserRequestDTO;
import it.softengunina.dietiestatesbackend.model.users.RealEstateManager;
import it.softengunina.dietiestatesbackend.repository.usersrepository.RealEstateManagerRepository;
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
class RealEstateAgentControllerTest {
    @Autowired
    RealEstateAgentController controller;
    @Autowired
    RealEstateManagerRepository managerRepository;

    RealEstateManager manager;
    UserRequestDTO req;

    @BeforeEach
    void setUp() {
        req = new UserRequestDTO("baseUserName");
        manager = managerRepository.findByBusinessUser_User_CognitoSub("manager1Sub").orElseThrow();
    }

    @Test
    void createAgent() {
        BusinessUserResponseDTO res = controller.createAgent(manager, req);
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("baseUserName", res.getUsername()),
                () -> assertNotNull(res.getAgency())
        );
    }
}