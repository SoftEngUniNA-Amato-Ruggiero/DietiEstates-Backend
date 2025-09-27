package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionForRentResponseDTO;
import it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto.InsertionWithRentResponseDTO;
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
        insertion = InsertionForRent.builder()
                .description("description")
                .tags(Set.of("tag1, tag2"))
                .address(address)
                .rent(90000.0)
                .uploader(uploader.getUser())
                .agency(uploader.getAgency())
                .build();
        visitor = new InsertionDTOVisitorImpl();
    }

    @Test
    void accept() {
        InsertionWithRentResponseDTO dto = insertion.accept(visitor);
        assertAll( () -> {
            assertInstanceOf(InsertionForRentResponseDTO.class, dto);
            assertEquals(insertion.getRent(), dto.getRent());
        });
    }
}