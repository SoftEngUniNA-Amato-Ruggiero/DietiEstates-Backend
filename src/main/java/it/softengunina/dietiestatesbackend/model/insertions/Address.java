package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String street;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;
}
