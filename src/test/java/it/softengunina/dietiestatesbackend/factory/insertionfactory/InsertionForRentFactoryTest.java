package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.InsertionForRentRequestDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionForRent;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class InsertionForRentFactoryTest {
    
    @Test
    void createInsertion() {
        Feature feature = getFeature();

        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.add(feature);

        InsertionForRentRequestDTO request = new InsertionForRentRequestDTO();
        request.setDescription("Villa in vendita in zona residenziale con stazione metro (che non esiste, infatti questo è un test");
        request.setTags(Set.of("giardino", "garage"));
        request.setAddress(featureCollection);
        request.setRent(500.0);

        RealEstateAgency agency = Mockito.mock (RealEstateAgency.class);
        var uploader = new it.softengunina.dietiestatesbackend.model.users.BusinessUser("Luigi", "Ruggiero", agency);

        InsertionForRentFactory insertionForRentFactory = new InsertionForRentFactory();

        InsertionForRent insertion = insertionForRentFactory.createInsertion(request, uploader);

        assertAll(
                () -> assertEquals("Villa in vendita in zona residenziale con stazione metro (che non esiste, infatti questo è un test", insertion.getDescription()),
                () -> assertEquals(Address.fromProperties(feature.getProperties()), insertion.getAddress()),
                () -> assertEquals(uploader.getUser(), insertion.getUploader()),
                () -> assertEquals(agency, insertion.getAgency()),
                () -> assertEquals(500.0, insertion.getRent()),
                () -> assertTrue(insertion.getTags().stream().anyMatch(tag -> tag.getName().equals("giardino"))),
                () -> assertTrue(insertion.getTags().stream().anyMatch(tag -> tag.getName().equals("garage")))
        );
    }

    private static Feature getFeature() {
        Feature feature = new Feature();
        Map<String, Object> properties = new HashMap<>();
        properties.put("street", "Via Roma");
        properties.put("number", "544");
        properties.put("city", "Melito di Napoli");
        properties.put("postalCode", "80017");
        properties.put("province", "NA");
        properties.put("country", "Italy");
        properties.put("lon", 14.195827);
        properties.put("lat", 40.922233);
        feature.setProperties(properties);
        return feature;
    }

}
