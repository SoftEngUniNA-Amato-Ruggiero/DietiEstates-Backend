package it.softengunina.dietiestatesbackend.model;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void builder() {
        Point location = new GeometryFactory().createPoint(new Coordinate(40.0, 15.0));

        Address address = Address.builder()
                .location(location)
                .countryCode("IT")
                .housenumber("10")
                .street("Via Roma")
                .country("Italy")
                .state("Lazio")
                .city("Roma")
                .postcode("00100")
                .build();

        assertAll(
                () -> assertEquals("IT", address.getCountryCode()),
                () -> assertEquals("10", address.getHousenumber()),
                () -> assertEquals("Via Roma", address.getStreet()),
                () -> assertEquals("Italy", address.getCountry()),
                () -> assertEquals("Lazio", address.getState()),
                () -> assertEquals("Roma", address.getCity()),
                () -> assertEquals("00100", address.getPostcode()),
                () -> assertEquals(location, address.getLocation())
        );
    }

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

        assertAll(
                () -> assertEquals("IT", address.getCountryCode()),
                () -> assertEquals("10", address.getHousenumber()),
                () -> assertEquals("Via Roma", address.getStreet()),
                () -> assertEquals("Italy", address.getCountry()),
                () -> assertEquals("Lazio", address.getState()),
                () -> assertEquals("Roma", address.getCity()),
                () -> assertEquals("00100", address.getPostcode()),
                () -> {
                    Point location = address.getLocation();
                    assertEquals(41.9028, location.getY(), 0.0001);
                    assertEquals(12.4964, location.getX(), 0.0001);
                }
        );
    }

}
