package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionWithPriceDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertionForSaleTest {
    InsertionForSale insertion;
    InsertionDTOVisitorImpl visitor;
    BusinessUser uploader;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new BusinessUser(new BaseUser("username", "sub"), agency);
        insertion = new InsertionForSale(new Address(), new InsertionDetails(), uploader.getUser(), uploader.getAgency(), 90000.0);
        visitor = new InsertionDTOVisitorImpl();
    }

    @Test
    void accept() {
        InsertionDTO dto = insertion.accept(visitor);
        assertAll( () -> {
            assertInstanceOf(InsertionWithPriceDTO.class, dto);
            assertEquals(insertion.getPrice(), ((InsertionWithPriceDTO) dto).getPrice());
        });
    }
}