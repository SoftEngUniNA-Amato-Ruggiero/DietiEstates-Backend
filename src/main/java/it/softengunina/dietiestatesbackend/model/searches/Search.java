package it.softengunina.dietiestatesbackend.model.searches;

import it.softengunina.dietiestatesbackend.model.AddressDetails;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Geometry;

//@Entity
//@Table(name = "searches")
//@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    @Setter
    private BaseUser user;

    @Column(columnDefinition = "geometry(Geometry, 4326)")
    @Getter
    @Setter
    private Geometry geometry;

    @Embedded
    @Getter
    @Setter
    private AddressDetails addressDetails;
}
