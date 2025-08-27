package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.services.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateAgentTest {
    RealEstateAgent agent;
    RealEstateAgency agency;
    PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionService = Mockito.mock(PromotionService.class);
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
        assertThrows(IllegalArgumentException.class, () -> agent.getPromotionToAgentFunction(promotionService));
    }

    @Test
    void getPromotionToManagerFunction() {
        Mockito.when(promotionService.promoteAgentToManager(agent)).thenReturn(new RealEstateManager(agent.getUsername(), agent.getCognitoSub(), agency));
        RealEstateManager manager = agent.getPromotionToManagerFunction(promotionService).apply(agency);
        assertAll(
                () -> assertNotNull(manager),
                () -> assertEquals(agent.getUsername(), manager.getUsername()),
                () -> assertEquals(agent.getCognitoSub(), manager.getCognitoSub()),
                () -> assertEquals(agent.getAgency(), manager.getAgency())
        );
    }

    @Test
    void getPromotionToManagerFunction_whenDifferentAgency() {
        Mockito.when(promotionService.promoteAgentToManager(agent)).thenReturn(new RealEstateManager(agent.getUsername(), agent.getCognitoSub(), agency));
        RealEstateAgency differentAgency = new RealEstateAgency("differentIban", "differentAgency");

        Function<RealEstateAgency, RealEstateManager> promotionFunction = agent.getPromotionToManagerFunction(promotionService);
        assertThrows(IllegalArgumentException.class, () -> promotionFunction.apply(differentAgency));
    }
}