package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.InsertionDTO;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.visitor.insertionsdtovisitor.InsertionDTOVisitor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
     * @param agency it is the real estate agency the uploader may be affiliated with
     * @param price it is the sale price of the real estate
     */
    public InsertionForSale(@NonNull Address address,
                            InsertionDetails details,
                            @NonNull BaseUser uploader,
                            RealEstateAgency agency,
                            double price) {
        super(address, details, uploader, agency);
        this.price = price;
    }

    /**
     * Accepts a visitor to produce a DTO
     * @param visitor it is the visitor which creates the DTO appropriate to this class
     * @return the DTO produced by the visitor
     */
    @Override
    public InsertionDTO accept(InsertionDTOVisitor visitor) {
        return visitor.visit(this);
    }
}
