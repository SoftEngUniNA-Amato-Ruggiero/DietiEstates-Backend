package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForRentRequestDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.softengunina.dietiestatesbackend.factory.insertionfactory.InsertionsFactoryTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class InsertionForRentFactoryTest {

    public static final double RENT = 500.0;

    InsertionForRentRequestDTO request;
    RealEstateAgency agency;
    BusinessUser uploader;

    InsertionForRentFactory insertionForRentFactory;

    @BeforeEach
    void setUp() {
        request = getInsertionForRentRequestDTO();
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new BusinessUser("email", "sub", agency);

        insertionForRentFactory = new InsertionForRentFactory();
    }

    /* ------------------ WHITE BOX TEST SUITE ------------------ */
    /* ------------------ InsertionForRentFactory.createInsertion ------------------ */

    @Test
    void createInsertion() {

        InsertionForRent insertion = insertionForRentFactory.createInsertion(request, uploader);

        assertAll(
                () -> assertEquals(uploader.getUser(), insertion.getUploader()),
                () -> assertEquals(agency, insertion.getAgency()),
                () -> assertEquals(DESCRIPTION, insertion.getDescription()),
                () -> assertEquals(LATITUDE, insertion.getAddress().getLocation().getY()),
                () -> assertEquals(LONGITUDE, insertion.getAddress().getLocation().getX()),
                () -> assertEquals(STREET_NAME, insertion.getAddress().getStreet()),
                () -> assertEquals(HOUSE_NUMBER, insertion.getAddress().getHousenumber()),
                () -> assertEquals(CITY_NAME, insertion.getAddress().getCity()),
                () -> assertEquals(POSTAL_CODE, insertion.getAddress().getPostcode()),
                () -> assertEquals(COUNTRY_NAME, insertion.getAddress().getCountry()),
                () -> assertEquals(RENT, insertion.getRent()),
                () -> assertTrue(insertion.getTags().stream().anyMatch(tag -> tag.getName().equals("giardino"))),
                () -> assertTrue(insertion.getTags().stream().anyMatch(tag -> tag.getName().equals("garage")))
        );
    }

    private InsertionForRentRequestDTO getInsertionForRentRequestDTO() {

        FeatureCollection featureCollection = getFeatureCollection();

        InsertionForRentRequestDTO req = new InsertionForRentRequestDTO();
        req.setAddress(featureCollection);
        req.setTags(TAGS);
        req.setDescription(DESCRIPTION);
        req.setRent(RENT);

        return req;
    }

}
