package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.services.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MeControllerIntegrationTest {
    @Autowired
    MeController meController;

    @MockitoBean
    TokenService tokenService;

    @Test
    void getMe_WhenBusinessUser() {
        Mockito.when(tokenService.getCognitoSub()).thenReturn("agent1Sub");
        assertDoesNotThrow(() -> meController.getMe());
    }

    @Test
    void getMe_WhenBaseUser() {
        Mockito.when(tokenService.getCognitoSub()).thenReturn("baseUserSub");
        assertThrows( ResponseStatusException.class, () -> meController.getMe() );
    }
}
