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
        agent = new RealEstateAgent(new BusinessUser(new BaseUser("testAgent", "testSub"), agency));
    }

    @Test
    void getAgency() {
        assertEquals(agency, agent.getAgency());
    }
}