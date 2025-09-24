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
                .countryCode((String) map.get("countryCode"))
                .housenumber((String) map.get("housenumber"))
                .street((String) map.get("street"))
                .country((String) map.get("country"))
                .state((String) map.get("state"))
                .stateCode((String) map.get("stateCode"))
                .district((String) map.get("district"))
                .city((String) map.get("city"))
                .suburb((String) map.get("suburb"))
                .county((String) map.get("county"))
                .countyCode((String) map.get("countyCode"))
                .resultType((String) map.get("resultType"))
                .postcode((String) map.get("postcode"))
                .formatted((String) map.get("formatted"))
                .addressLine1((String) map.get("addressLine1"))
                .addressLine2((String) map.get("addressLine2"))
                .plusCode((String) map.get("plusCode"))
                .plusCodeShort((String) map.get("plusCodeShort"))
                .iso3166Dash2((String) map.get("iso3166_2"))
                .placeId((String) map.get("placeId"))
                .build();
    }

}
