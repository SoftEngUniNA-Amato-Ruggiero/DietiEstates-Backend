package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;
    RealEstateAgent agent;
    RealEstateManager manager;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("testIban", "testAgency");
        user = new User("testUser", "testSub1");
        agent = new RealEstateAgent("testAgent", "testSub2", agency);
        manager = new RealEstateManager("testManager", "testSub3", agency);
    }

    @Test
    void getRoleUser() {
        assertEquals("User", user.getRole());
    }

    @Test
    void getRoleAgent() {
        assertEquals("RealEstateAgent", agent.getRole());
    }

    @Test
    void getRoleManager() {
        assertEquals("RealEstateManager", manager.getRole());
    }

    @Test
    void getAgencyUser() {
        assertNull(user.getAgency());
    }

    @Test
    void getAgencyAgent() {
        assertEquals(agency, agent.getAgency());
    }
}