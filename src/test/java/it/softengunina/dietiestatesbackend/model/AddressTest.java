package it.softengunina.dietiestatesbackend.model;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressTest {

    @Test
    void fromProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("country_code", "IT");
        properties.put("housenumber", "10");
        properties.put("street", "Via Roma");
        properties.put("country", "Italy");
        properties.put("state", "Lazio");
        properties.put("city", "Roma");
        properties.put("postcode", "00100");
        properties.put("lat", 41.9028);
        properties.put("lon", 12.4964);

        Address address = Address.fromProperties(properties);
        assert address.getCountryCode().equals("IT");
        assert address.getHousenumber().equals("10");
        assert address.getStreet().equals("Via Roma");
        assert address.getCountry().equals("Italy");
        assert address.getState().equals("Lazio");
        assert address.getCity().equals("Roma");
        assert address.getPostcode().equals("00100");

        Point location = address.getLocation();
        assertEquals(41.9028, location.getY(), 0.0001);
        assertEquals(12.4964, location.getX(), 0.0001);

    }

}
