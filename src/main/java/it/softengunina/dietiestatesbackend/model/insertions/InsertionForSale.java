package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.users.RealEstateAgent;
import it.softengunina.dietiestatesbackend.factory.insertiondtofactory.InsertionDTOFactory;
import it.softengunina.dietiestatesbackend.factory.insertiondtofactory.InsertionWithPriceDTOFactory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;

/**
 * Class for insertions about real estates that are available for sale.
 */
@Entity
@Table(name = "insertions_for_sale")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsertionForSale extends BaseInsertion implements InsertionWithPrice {
    @Getter
    @Setter
    private Double price;

    /**
     * Constructor for InsertionForSale.
     * @param address it is the address of the insertion
     * @param details it contains all the details of the insertion
     * @param uploader it is the real estate agent who uploaded the insertion
     * @param price it is the sale price of the real estate
     */
    public InsertionForSale(Address address, InsertionDetails details, RealEstateAgent uploader, double price) {
        super(address, details, uploader);
        this.price = price;
    }

    @Override
    @Transient
    public InsertionDTOFactory getDTOFactory() {
        return new InsertionWithPriceDTOFactory(this);
    }
}
