package it.softengunina.dietiestatesbackend.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Map;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class AddressDetails {
    private String countryCode;
    private String housenumber;
    private String street;
    private String country;
    private String state;
    private String stateCode;
    private String district;
    private String city;
    private String suburb;
    private String county;
    private String countyCode;
    private String resultType;
    private String postcode;
    private String formatted;
    private String addressLine1;
    private String addressLine2;
    private String plusCode;
    private String plusCodeShort;
    private String iso3166Dash2;
    private String placeId;

    public static AddressDetails fromProperties(Map<String, Object> map) {
        return AddressDetails.builder()
                .countryCode(String.valueOf(map.get("country_code")))
                .housenumber(String.valueOf(map.get("housenumber")))
                .street(String.valueOf(map.get("street")))
                .country(String.valueOf(map.get("country")))
                .state(String.valueOf(map.get("state")))
                .stateCode(String.valueOf(map.get("state_code")))
                .district(String.valueOf(map.get("district")))
                .city(String.valueOf(map.get("city")))
                .suburb(String.valueOf(map.get("suburb")))
                .county(String.valueOf(map.get("county")))
                .countyCode(String.valueOf(map.get("county_code")))
                .resultType(String.valueOf(map.get("result_type")))
                .postcode(String.valueOf(map.get("postcode")))
                .formatted(String.valueOf(map.get("formatted")))
                .addressLine1(String.valueOf(map.get("address_line1")))
                .addressLine2(String.valueOf(map.get("address_line2")))
                .plusCode(String.valueOf(map.get("plus_code")))
                .plusCodeShort(String.valueOf(map.get("plus_code_short")))
                .iso3166Dash2(String.valueOf(map.get("iso3166_2")))
                .placeId(String.valueOf(map.get("placeId")))
                .build();
    }

}
