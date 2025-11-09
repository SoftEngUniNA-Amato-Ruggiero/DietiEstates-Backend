package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.usersdto.MeResponseDTO;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsServiceImpl;
import it.softengunina.dietiestatesbackend.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    TokenService tokenService;
    @MockitoBean
    NotificationsServiceImpl notificationsService;

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

    @Test
    void postMe() {
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(tokenService.getJwt()).thenReturn(jwt);
        Mockito.when(tokenService.getCognitoSub(jwt)).thenReturn("newUserSub");
        Mockito.when(tokenService.getEmail(jwt)).thenReturn("newuser@email.com");
        Mockito.doAnswer(i -> {
                    NotificationsPreferences prefs = i.getArgument(0);
                    prefs.setSubscriptionArn("subscriptionArn");
                    return null;
                })
                .when(notificationsService)
                .enableEmailSubscription(Mockito.any(NotificationsPreferences.class));

        MeResponseDTO newUser = meController.postMe();

        assertAll(
                () -> assertNotNull(baseUserRepository.findById(newUser.getId())),
                () -> assertNotNull(newUser),
                () -> assertNotNull(newUser.getNotificationsPreferences()),
                () -> assertEquals("newuser@email.com", newUser.getUsername()),
                () -> assertTrue(newUser.getRoles().isEmpty())
        );
    }
}
