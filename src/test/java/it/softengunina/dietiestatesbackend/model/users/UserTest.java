package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.services.PromotionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;
    RealEstateAgency agency;
    PromotionServiceImpl strategyService;

    @BeforeEach
    void setUp() {
        strategyService = Mockito.mock(PromotionServiceImpl.class);
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
    void getPromotionToAgentCommand() {
        assertNotNull(user.getPromotionToAgentCommand(agency));
    }

    @Test
    void getPromotionToManagerCommand() {
        assertNotNull(user.getPromotionToManagerCommand(agency));
    }
}