package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.factory.InsertionDTOFactory;
import it.softengunina.dietiestatesbackend.factory.InsertionWithRentDTOFactory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Transient;

@Entity
@Table(name = "insertions_for_rent")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsertionForRent extends BaseInsertion implements InsertionWithRent {
    @Getter
    @Setter
    private Double rent;

    public InsertionForRent(Address address, InsertionDetails details, RealEstateAgent uploader, double rent) {
        super(address, details, uploader);
        this.rent = rent;
    }

    @Override
    @Transient
    public InsertionDTOFactory getDTOFactory() {
        return new InsertionWithRentDTOFactory(this);
    }
}
