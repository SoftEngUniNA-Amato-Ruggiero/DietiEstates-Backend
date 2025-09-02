package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithRentDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertionForRentTest {
    InsertionForRent insertion;
    InsertionDTOVisitorImpl visitor;
    UserWithAgency uploader;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new RealEstateAgent(new BaseUser("username", "sub"), agency);
        insertion = new InsertionForRent(new Address(), new InsertionDetails(), uploader.getUser(), uploader.getAgency(), 900.0);
        visitor = new InsertionDTOVisitorImpl();
    }

    @Test
    void accept() {
        InsertionDTO dto = insertion.accept(visitor);
        assertAll( () -> {
            assertInstanceOf(InsertionWithRentDTO.class, dto);
            assertEquals(insertion.getRent(), ((InsertionWithRentDTO) dto).getRent());
        });
    }
}