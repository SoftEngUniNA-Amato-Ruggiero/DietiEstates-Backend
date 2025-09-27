package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RealEstateManagerTest {

    RealEstateManager manager;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("testIban", "testAgency");
        manager = new RealEstateManager(new BusinessUser(new BaseUser("testManager", "testSub"), agency));
    }

    @Test
    void getAgency() {
        assertEquals(agency, manager.getAgency());
    }

    @Test
    void getSpecificRoleName() {
        assertEquals("RealEstateManager", manager.getSpecificRoleName());
    }

}
