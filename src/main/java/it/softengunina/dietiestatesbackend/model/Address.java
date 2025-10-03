package it.softengunina.dietiestatesbackend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Delegate;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.Map;

@Entity
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Embedded
    @Column(nullable = false)
    @Getter
    @Setter
    @Delegate(types = AddressDetails.class)
    private AddressDetails details;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    @Getter
    @Setter
    private Point location;

    public Address(@NonNull AddressDetails details,
                   @NonNull Point location) {
        this.details = details;
        this.location = location;
    }

    @Builder
    public Address(@NonNull Point location,
                    String countryCode,
                    String housenumber,
                    String street,
                    String country,
                    String state,
                    String stateCode,
                    String district,
                    String city,
                    String suburb,
                    String county,
                    String countyCode,
                    String resultType,
                    String postcode,
                    String formatted,
                    String addressLine1,
                    String addressLine2,
                    String plusCode,
                    String plusCodeShort,
                    String iso3166Dash2,
                    String placeId) {
        this.location = location;
        this.details = AddressDetails.builder()
                .countryCode(countryCode)
                .housenumber(housenumber)
                .street(street)
                .country(country)
                .state(state)
                .stateCode(stateCode)
                .district(district)
                .city(city)
                .suburb(suburb)
                .county(county)
                .countyCode(countyCode)
                .resultType(resultType)
                .postcode(postcode)
                .formatted(formatted)
                .addressLine1(addressLine1)
                .addressLine2(addressLine2)
                .plusCode(plusCode)
                .plusCodeShort(plusCodeShort)
                .iso3166Dash2(iso3166Dash2)
                .placeId(placeId)
                .build();
    }

    public static Address fromProperties(Map<String, Object> properties) {
        AddressDetails details = AddressDetails.fromProperties(properties);

        Double lat = (Double) properties.get("lat");
        Double lon = (Double) properties.get("lon");
        Coordinate coordinate = new Coordinate(lon, lat);
        Point location = new GeometryFactory().createPoint(coordinate);

        return new Address(details, location);
    }
}
