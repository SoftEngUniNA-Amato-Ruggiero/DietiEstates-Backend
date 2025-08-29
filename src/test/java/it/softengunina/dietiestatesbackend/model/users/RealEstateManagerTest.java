package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.ImpossiblePromotionException;
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
        manager = new RealEstateManager("testManager", "testSub", agency);
    }

    @Test
    void getRole() {
        assertEquals("RealEstateManager", manager.getRole());
    }

    @Test
    void getPromotionToManagerFunction() {
        assertThrows(ImpossiblePromotionException.class, () -> manager.getPromotionToManagerFunction(agency));
    }
}