package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Address {
    private String street;
    private int houseNumber;
    private String city;
    private int postalCode;
}
