package it.softengunina.dietiestatesbackend.controller.userscontroller;

import it.softengunina.dietiestatesbackend.dto.usersdto.BusinessUserResponseDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserRequestDTO;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RealEstateManagerControllerTest {
    @Autowired
    RealEstateManagerController controller;

    @MockitoBean
    TokenService tokenService;

    UserRequestDTO req;

    @BeforeEach
    void setUp() {
        req = new UserRequestDTO("baseUserName");
        Mockito.when(tokenService.getCognitoSub())
                .thenReturn("manager1Sub");
    }

    @Test
    void createManager() {
        BusinessUserResponseDTO res = controller.createManager(req);
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("baseUserName", res.getUsername()),
                () -> assertNotNull(res.getAgency())
        );
    }
}