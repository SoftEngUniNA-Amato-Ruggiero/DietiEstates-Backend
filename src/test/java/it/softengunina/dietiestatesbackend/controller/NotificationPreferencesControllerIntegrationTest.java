package it.softengunina.dietiestatesbackend.controller;

import it.softengunina.dietiestatesbackend.dto.NotificationsPreferencesDTO;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.NotificationsPreferencesRepository;
import it.softengunina.dietiestatesbackend.repository.usersrepository.BaseUserRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsServiceImpl;
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
class NotificationPreferencesControllerIntegrationTest {
    @Autowired
    NotificationPreferencesController controller;
    @Autowired
    BaseUserRepository userRepository;
    @Autowired
    NotificationsPreferencesRepository preferencesRepository;

    @MockitoBean
    NotificationsServiceImpl notificationsService;

    BaseUser user;
    NotificationsPreferencesDTO req;

    @BeforeEach
    void setUp() {

        Mockito.doCallRealMethod().when(notificationsService).toggleEmailSubscription(Mockito.any(NotificationsPreferences.class));

        Mockito.doAnswer(invocation -> {
            NotificationsPreferences p = invocation.getArgument(0);
            if (!p.isEmailNotificationsEnabled()) {
                p.setSubscriptionArn("subscriptionArn");
            }
            return null;
        }).when(notificationsService).enableEmailSubscription(Mockito.any(NotificationsPreferences.class));

        Mockito.doAnswer(invocation -> {
            NotificationsPreferences p = invocation.getArgument(0);
            if (p.isEmailNotificationsEnabled()) {
                p.setSubscriptionArn("");
            }
            return null;
        }).when(notificationsService).disableEmailSubscription(Mockito.any(NotificationsPreferences.class));

    }

    @Test
    void updatePreferences() {
        user = userRepository.findById(10L).orElseThrow();
        req = NotificationsPreferencesDTO.builder()
                .city("TestCity")
                .emailNotificationsEnabled(true)
                .notificationsForSaleEnabled(true)
                .notificationsForRentEnabled(false)
                .build();


        assertDoesNotThrow(() -> controller.updatePreferences(user, req));
        NotificationsPreferences updatedPrefs = preferencesRepository.findByUser(user).orElseThrow();

        assertAll(
                () -> assertEquals(req.getCity(), updatedPrefs.getCity()),
                () -> assertEquals(req.getNotificationsForSaleEnabled(), updatedPrefs.isNotificationsForSaleEnabled()),
                () -> assertEquals(req.getNotificationsForRentEnabled(), updatedPrefs.isNotificationsForRentEnabled()),
                () -> assertTrue(updatedPrefs.isEmailNotificationsEnabled()),
                () -> assertNotNull(updatedPrefs.getSubscriptionArn())
        );
    }
}