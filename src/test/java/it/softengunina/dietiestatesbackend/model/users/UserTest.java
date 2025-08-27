package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.services.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;
    RealEstateAgency agency;
    PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionService = Mockito.mock(PromotionService.class);
        user = new User("testUser", "testSub1");
        agency = new RealEstateAgency("testIban", "testAgency");
    }

    @Test
    void getRoleUser() {
        assertEquals("User", user.getRole());
    }

    @Test
    void getAgencyUser() {
        assertNull(user.getAgency());
    }

    @Test
    void getPromotionToAgentFunction() {
        Mockito.when(promotionService.promoteUserToAgent(user, agency)).thenReturn(new RealEstateAgent(user.getUsername(), user.getCognitoSub(), agency));
        UserWithAgency agent = user.getPromotionToAgentFunction(promotionService).apply(agency);
        assertAll(
                () -> assertNotNull(agent),
                () -> assertEquals(user.getUsername(), agent.getUsername()),
                () -> assertEquals(user.getCognitoSub(), agent.getCognitoSub()),
                () -> assertEquals(agency, agent.getAgency()),
                () -> assertEquals("RealEstateAgent", agent.getRole()),
                () -> assertFalse(agent.isManager())
        );
    }

    @Test
    void getPromotionToManagerFunction() {
        Mockito.when(promotionService.promoteUserToManager(user, agency)).thenReturn(new RealEstateManager(user.getUsername(), user.getCognitoSub(), agency));
        UserWithAgency manager = user.getPromotionToManagerFunction(promotionService).apply(agency);
        assertAll(
                () -> assertNotNull(manager),
                () -> assertEquals(user.getUsername(), manager.getUsername()),
                () -> assertEquals(user.getCognitoSub(), manager.getCognitoSub()),
                () -> assertEquals(agency, manager.getAgency()),
                () -> assertEquals("RealEstateManager", manager.getRole()),
                () -> assertTrue(manager.isManager())
        );
    }
}