package it.softengunina.dietiestatesbackend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

class AddressDetailsTest {

    @Test
    void fromProperties() {
        Map<String, Object> properties = getStringObjectMap();

        AddressDetails details = AddressDetails.fromProperties(properties);

        assertEquals("IT", details.getCountryCode());
        assertEquals("10", details.getHousenumber());
        assertEquals("Via Roma", details.getStreet());
        assertEquals("Italia", details.getCountry());
        assertEquals("Lazio", details.getState());
        assertEquals("LAZ", details.getStateCode());
        assertEquals("Centro", details.getDistrict());
        assertEquals("Roma", details.getCity());
        assertEquals("Trastevere", details.getSuburb());
        assertEquals("Roma", details.getCounty());
        assertEquals("RM", details.getCountyCode());
        assertEquals("address", details.getResultType());
        assertEquals("00100", details.getPostcode());
        assertEquals("Via Roma 10, 00100 Roma", details.getFormatted());
        assertEquals("Via Roma 10", details.getAddressLine1());
        assertEquals("00100 Roma", details.getAddressLine2());
        assertEquals("8FH4V2V3+9C", details.getPlusCode());
        assertEquals("V2V3+9C", details.getPlusCodeShort());
        assertEquals("IT-LAZ", details.getIso3166Dash2());
        assertEquals("123456", details.getPlaceId());
    }

    private static Map<String, Object> getStringObjectMap() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("country_code", "IT");
        properties.put("housenumber", "10");
        properties.put("street", "Via Roma");
        properties.put("country", "Italia");
        properties.put("state", "Lazio");
        properties.put("state_code", "LAZ");
        properties.put("district", "Centro");
        properties.put("city", "Roma");
        properties.put("suburb", "Trastevere");
        properties.put("county", "Roma");
        properties.put("county_code", "RM");
        properties.put("result_type", "address");
        properties.put("postcode", "00100");
        properties.put("formatted", "Via Roma 10, 00100 Roma");
        properties.put("address_line1", "Via Roma 10");
        properties.put("address_line2", "00100 Roma");
        properties.put("plus_code", "8FH4V2V3+9C");
        properties.put("plus_code_short", "V2V3+9C");
        properties.put("iso3166_2", "IT-LAZ");
        properties.put("placeId", "123456");
        return properties;
    }
}

