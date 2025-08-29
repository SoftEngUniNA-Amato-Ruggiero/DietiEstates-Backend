package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * This class represents an address with street, house number, city, and postal code.
 * It is marked as embeddable to be used within other entity classes, in fact it hasn't
 * its own table in the database nor a primary key.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Address {
    private String street;
    private Short houseNumber;
    private String city;
    private String postalCode;
}
