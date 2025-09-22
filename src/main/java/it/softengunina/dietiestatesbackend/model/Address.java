package it.softengunina.dietiestatesbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String city;

    @Column(nullable = false)
    @Getter
    @Setter
    private String province;

    @Column(nullable = false)
    @Getter
    @Setter
    private String postalCode;

    @Column(nullable = false)
    @Getter
    @Setter
    private String street;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    @Getter
    @Setter
    private Point location;

    public Address(@NonNull String city,
                   @NonNull String province,
                   @NonNull String postalCode,
                   @NonNull String street,
                   @NonNull Point location) {
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.street = street;
        this.location = location;
    }
}
