package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateAgentTest {
    RealEstateAgent agent;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
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
    void getPromotionToAgentCommand() {
        assertThrows(IllegalArgumentException.class, () -> agent.getPromotionToAgentCommand(agency));
    }

    @Test
    void getPromotionToManagerCommand() {
        assertNotNull(agent.getPromotionToManagerCommand(agency));
    }

    @Test
    void getPromotionToManagerCommand_differentAgency() {
        RealEstateAgency differentAgency = new RealEstateAgency("differentIban", "differentAgency");
        assertThrows(IllegalArgumentException.class, () -> agent.getPromotionToManagerCommand(differentAgency));
    }
}