package it.softengunina.userservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RealEstateAgencyTest {
    RealEstateAgency agency;
    RealEstateAgent existingAgent;
    RealEstateManager existingManager;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("testIban", "testAgencyName");
        existingAgent = new RealEstateAgent("existingagent@email.com", "existingAgentCognitoSub", agency);
        existingManager = new RealEstateManager("existingmanager@email.com", "existingManagerCognitoSub", agency);
        agency.addAgent(existingAgent);
        agency.addManager(existingManager);
    }

    @Test
    void addManager() {
        RealEstateManager manager = new RealEstateManager("manager@email.com", "managerCognitoSub", agency);
        agency.addManager(manager);
        assertTrue(agency.getManagers().contains(manager));
    }

    @Test
    void addAgent() {
        RealEstateAgent agent = new RealEstateAgent("agent@email.com", "agentCognitoSub", agency);
        agency.addAgent(agent);
        assertTrue(agency.getAgents().contains(agent));
    }

    @Test
    void removeManager() {
        agency.removeManager(existingManager);
        assertFalse(agency.getManagers().contains(existingManager));
    }

    @Test
    void removeAgent() {
        agency.removeAgent(existingAgent);
        assertFalse(agency.getAgents().contains(existingAgent));
    }
}