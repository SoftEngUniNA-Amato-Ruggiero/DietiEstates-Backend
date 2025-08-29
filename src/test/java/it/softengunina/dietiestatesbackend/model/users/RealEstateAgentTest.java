package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.ImpossiblePromotionException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateAgentTest {
    RealEstateAgent agent;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("testIban", "testAgency");
        agent = new RealEstateAgent("testAgent", "testSub", agency);
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
        RealEstateAgency agentAgency = agent.getAgency();
        assertThrows(ImpossiblePromotionException.class, () -> agent.getPromotionToAgentFunction(agentAgency));
    }

    @Test
    void getPromotionToManagerFunction() {
        UserPromotionStrategy promotionService = Mockito.mock(UserPromotionStrategy.class);
        Mockito.when(promotionService.promoteAgentToManager(agent)).thenReturn(new RealEstateManager(agent.getUsername(), agent.getCognitoSub(), agent.getAgency()));

        UserWithAgency manager = agent.getPromotionToManagerFunction(agent.getAgency()).apply(promotionService);
        assertAll(
                () -> assertNotNull(manager),
                () -> assertEquals(agent.getUsername(), manager.getUsername()),
                () -> assertEquals(agent.getCognitoSub(), manager.getCognitoSub()),
                () -> assertEquals(agent.getAgency(), manager.getAgency()),
                () -> assertEquals("RealEstateManager", manager.getRole())
        );
    }

    @Test
    void getPromotionToManagerFunction_whenDifferentAgency() {
        UserPromotionStrategy promotionService = Mockito.mock(UserPromotionStrategy.class);
        Mockito.when(promotionService.promoteAgentToManager(agent)).thenReturn(new RealEstateManager(agent.getUsername(), agent.getCognitoSub(), agent.getAgency()));

        RealEstateAgency differentAgency = new RealEstateAgency("differentIban", "differentAgency");
        assertThrows(ImpossiblePromotionException.class, () -> agent.getPromotionToManagerFunction(differentAgency));
    }
}