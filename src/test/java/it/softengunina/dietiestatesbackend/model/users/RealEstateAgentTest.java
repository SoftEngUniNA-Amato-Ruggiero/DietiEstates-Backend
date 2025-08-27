package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.ImpossiblePromotionException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateAgentTest {
    RealEstateAgent agent;
    RealEstateAgency agency;
    UserPromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionService = Mockito.mock(UserPromotionService.class);
        agency = new RealEstateAgency("testIban", "testAgency");
        agent = new RealEstateAgent("testAgent", "testSub2", agency);
    }

    @Test
    void getRole() {
        assertEquals("RealEstateAgent", agent.getRole());
    }

    @Test
    void getAgency() {
        assertEquals(agency, agent.getAgency());
    }

    @Test
    void getPromotionToAgentFunction() {
        assertThrows(ImpossiblePromotionException.class, () -> agent.getPromotionToAgentFunction(promotionService));
    }

    @Test
    void getPromotionToManagerFunction() {
        Mockito.when(promotionService.promoteAgentToManager(agent)).thenReturn(new RealEstateManager(agent.getUsername(), agent.getCognitoSub(), agency));
        UserWithAgency manager = agent.getPromotionToManagerFunction(promotionService).apply(agency);
        assertAll(
                () -> assertNotNull(manager),
                () -> assertEquals(agent.getUsername(), manager.getUsername()),
                () -> assertEquals(agent.getCognitoSub(), manager.getCognitoSub()),
                () -> assertEquals(agent.getAgency(), manager.getAgency()),
                () -> assertEquals("RealEstateManager", manager.getRole()),
                () -> assertTrue(manager.isManager())
        );
    }

    @Test
    void getPromotionToManagerFunction_whenDifferentAgency() {
        Mockito.when(promotionService.promoteAgentToManager(agent)).thenReturn(new RealEstateManager(agent.getUsername(), agent.getCognitoSub(), agency));
        RealEstateAgency differentAgency = new RealEstateAgency("differentIban", "differentAgency");

        Function<RealEstateAgency, UserWithAgency> promotionFunction = agent.getPromotionToManagerFunction(promotionService);
        assertThrows(ImpossiblePromotionException.class, () -> promotionFunction.apply(differentAgency));
    }
}