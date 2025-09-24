package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithRentDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class InsertionForRentTest {
    InsertionForRent insertion;
    InsertionDTOVisitorImpl visitor;
    BusinessUser uploader;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        Address address = mock(Address.class);

        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new BusinessUser(new BaseUser("username", "sub"), agency);
        insertion = new InsertionForRent(address, new InsertionDetails(), uploader.getUser(), uploader.getAgency(), 900.0);
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