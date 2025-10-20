package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForSaleRequestDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForSale;
import it.softengunina.dietiestatesbackend.model.users.BusinessUser;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.softengunina.dietiestatesbackend.factory.insertionfactory.InsertionsFactoryTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class InsertionForSaleFactoryTest {

    public static final double PRICE = 50000.0;

    InsertionForSaleRequestDTO request;
    RealEstateAgency agency;
    BusinessUser uploader;

    InsertionForSaleFactory insertionForSaleFactory;

    @BeforeEach
    void setUp() {
        request = getInsertionForSaleRequestDTO();
        agency = new RealEstateAgency("iban", "agencyName");
        uploader = new BusinessUser("email", "sub", agency);

        insertionForSaleFactory = new InsertionForSaleFactory();
    }


    /* ------------------ WHITE BOX TEST SUITE ------------------ */
    /* ------------------ InsertionForSaleFactory.createInsertion ------------------ */

    @Test
    void createInsertion() {

        InsertionForSale insertion = insertionForSaleFactory.createInsertion(request, uploader);

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
                () -> assertEquals(PRICE, insertion.getPrice()),
                () -> assertTrue(insertion.getTags().stream().anyMatch(tag -> tag.getName().equals("giardino"))),
                () -> assertTrue(insertion.getTags().stream().anyMatch(tag -> tag.getName().equals("garage")))
        );
    }

    private InsertionForSaleRequestDTO getInsertionForSaleRequestDTO() {

        FeatureCollection featureCollection = getFeatureCollection();

        InsertionForSaleRequestDTO req = new InsertionForSaleRequestDTO();
        req.setAddress(featureCollection);
        req.setTags(TAGS);
        req.setDescription(DESCRIPTION);
        req.setPrice(PRICE);

        return req;
    }
}
