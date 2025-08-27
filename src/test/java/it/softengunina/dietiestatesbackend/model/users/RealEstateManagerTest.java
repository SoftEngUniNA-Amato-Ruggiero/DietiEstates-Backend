package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.exceptions.ImpossiblePromotionException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RealEstateManagerTest {
    RealEstateManager manager;
    RealEstateAgency agency;
    UserPromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionService = Mockito.mock(UserPromotionService.class);
        agency = new RealEstateAgency("testIban", "testAgency");
        manager = new RealEstateManager("testManager", "testSub3", agency);
    }

    @Test
    void getRole() {
        assertEquals("RealEstateManager", manager.getRole());
    }

    @Test
    void getPromotionToManagerFunction() {
        assertThrows(ImpossiblePromotionException.class, () -> manager.getPromotionToManagerFunction(promotionService));
    }
}