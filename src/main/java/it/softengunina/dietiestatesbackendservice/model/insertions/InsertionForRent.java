package it.softengunina.dietiestatesbackendservice.model.insertions;

import it.softengunina.dietiestatesbackendservice.model.users.RealEstateAgent;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsertionForRent extends Insertion {
    private double rent;

    public InsertionForRent(Address address, InsertionDetails details, RealEstateAgent uploader, double rent) {
        super(address, details, uploader);
        this.rent = rent;
    }
}
