package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionForSaleResponseDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionWithPriceResponseDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class InsertionForSaleTest {
    InsertionForSale insertion;
    InsertionDTOVisitorImpl visitor;
    BusinessUser uploader;
    RealEstateAgency agency;

    @BeforeEach
    void setUp() {
        Address address = mock(Address.class);
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new BusinessUser(new BaseUser("username", "sub"), agency);
        insertion = InsertionForSale.builder()
                .description("description")
                .tags(Tag.fromNames(Set.of("tag1, tag2")))
                .address(address)
                .price(90000.0)
                .uploader(uploader.getUser())
                .agency(uploader.getAgency())
                .build();
        visitor = new InsertionDTOVisitorImpl();
    }

    @Test
    void accept() {
        InsertionWithPriceResponseDTO dto = insertion.accept(visitor);
        assertAll( () -> {
            assertInstanceOf(InsertionForSaleResponseDTO.class, dto);
            assertEquals(insertion.getPrice(), dto.getPrice());
        });
    }
}