package it.softengunina.dietiestatesbackend.dto;

import it.softengunina.dietiestatesbackend.model.Address;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class AddressDTO {
    private Point location;
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

    public AddressDTO(Address address) {
        this.location = address.getLocation();
        this.countryCode = address.getCountryCode();
        this.housenumber = address.getHousenumber();
        this.street = address.getStreet();
        this.country = address.getCountry();
        this.state = address.getState();
        this.stateCode = address.getStateCode();
        this.district = address.getDistrict();
        this.city = address.getCity();
        this.suburb = address.getSuburb();
        this.county = address.getCounty();
        this.countyCode = address.getCountyCode();
        this.resultType = address.getResultType();
        this.postcode = address.getPostcode();
        this.formatted = address.getFormatted();
        this.addressLine1 = address.getAddressLine1();
        this.addressLine2 = address.getAddressLine2();
        this.plusCode = address.getPlusCode();
        this.plusCodeShort = address.getPlusCodeShort();
        this.iso3166Dash2 = address.getIso3166Dash2();
        this.placeId = address.getPlaceId();
    }
}
