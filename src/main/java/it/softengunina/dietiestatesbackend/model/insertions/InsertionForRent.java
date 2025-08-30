package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.factory.insertiondtofactory.InsertionDTOFactory;
import it.softengunina.dietiestatesbackend.factory.insertiondtofactory.InsertionWithRentDTOFactory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Transient;

/**
 * Class for insertions about real estates that are available for rent.
 */
@Entity
@Table(name = "insertions_for_rent")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsertionForRent extends BaseInsertion implements InsertionWithRent {
    @Getter
    @Setter
    private Double rent;

    /**
     * Constructor for InsertionForRent.
     * @param address it is the address of the insertion
     * @param details it contains all the details of the insertion
     * @param uploader it is the real estate agent who uploaded the insertion
     * @param rent it is the monthly rent of the real estate
     */
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
