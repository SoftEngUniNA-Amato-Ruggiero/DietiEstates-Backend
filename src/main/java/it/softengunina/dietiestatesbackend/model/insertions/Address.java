package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.locationtech.jts.geom.Point;


@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Address {
    private String address;

    @Column(columnDefinition = "varchar(255)")
    private Point location;
}
