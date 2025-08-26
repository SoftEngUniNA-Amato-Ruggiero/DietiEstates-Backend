package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateManagerTest {
    RealEstateManager manager;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("testIban", "testAgency");
        manager = new RealEstateManager("testManager", "testSub3", agency);
    }

    @Test
    void getRole() {
        assertEquals("RealEstateManager", manager.getRole());
    }

    @Test
    void getPromotionToManagerCommand() {
        assertThrows(IllegalArgumentException.class, () -> manager.getPromotionToManagerCommand(agency));
    }
}