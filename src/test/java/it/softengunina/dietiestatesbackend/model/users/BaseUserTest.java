package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BaseUserTest {
    BaseUser user;
    RealEstateAgency agency;
    UserPromotionStrategy promotionService;

    @BeforeEach
    void setUp() {
        promotionService = Mockito.mock(UserPromotionStrategy.class);
        user = new BaseUser("testUser", "testSub1");
        agency = new RealEstateAgency("testIban", "testAgency");
    }

    @Test
    void getRoleUser() {
        assertEquals("BaseUser", user.getRole());
    }

    @Test
    void getAgencyUser() {
        assertNull(user.getAgency());
    }

    @Test
    void getPromotionToAgentFunction() {
        Mockito.when(promotionService.promoteUserToAgent(user, agency)).thenReturn(new RealEstateAgent(user.getUsername(), user.getCognitoSub(), agency));
        UserWithAgency agent = user.getPromotionToAgentFunction(agency).apply(promotionService);
        assertAll(
                () -> assertNotNull(agent),
                () -> assertEquals(user.getId(), agent.getId()),
                () -> assertEquals(user.getUsername(), agent.getUsername()),
                () -> assertEquals(user.getCognitoSub(), agent.getCognitoSub()),
                () -> assertEquals(agency, agent.getAgency()),
                () -> assertEquals("RealEstateAgent", agent.getRole()),
                () -> assertFalse(agent.isManager())
        );
    }

    @Test
    void getPromotionToManagerFunction() {
        Mockito.when(promotionService.promoteUserToAgent(user, agency)).thenReturn(new RealEstateAgent(user.getUsername(), user.getCognitoSub(), agency));
        Mockito.when(promotionService.promoteAgentToManager(Mockito.any(UserWithAgency.class))).thenAnswer(invocation -> {
            UserWithAgency arg = invocation.getArgument(0);
            return new RealEstateManager(arg.getUsername(), arg.getCognitoSub(), arg.getAgency());
        });

        UserWithAgency manager = user.getPromotionToManagerFunction(agency).apply(promotionService);
        assertAll(
                () -> assertNotNull(manager),
                () -> assertEquals(user.getId(), manager.getId()),
                () -> assertEquals(user.getUsername(), manager.getUsername()),
                () -> assertEquals(user.getCognitoSub(), manager.getCognitoSub()),
                () -> assertEquals(agency, manager.getAgency()),
                () -> assertEquals("RealEstateManager", manager.getRole()),
                () -> assertTrue(manager.isManager())
        );
    }
}